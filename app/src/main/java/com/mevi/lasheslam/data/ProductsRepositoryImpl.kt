package com.mevi.lasheslam.data

import android.net.Uri
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.data.constants.StoragePaths
import com.mevi.lasheslam.domain.model.CartItem
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.repository.ProductsRepository
import com.mevi.lasheslam.domain.model.CategoryModel
import com.mevi.lasheslam.data.dto.CreateProductDto
import com.mevi.lasheslam.domain.model.ProductDetail
import com.mevi.lasheslam.domain.model.ProductItem
import com.mevi.lasheslam.data.dto.ProductItemDto
import com.mevi.lasheslam.data.dto.toDetail
import com.mevi.lasheslam.data.dto.toDomain
import com.mevi.lasheslam.data.dto.toDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val errorMapper: ErrorMapper
) : ProductsRepository {

    override suspend fun createProduct(product: CreateProductModel): Resource<Unit> {
        return try {
            val id = UUID.randomUUID().toString()
            val productsImageUrl = uploadProductsImages(productId = id, images = product.images)
            val dto = product.toDto(id = id, images = productsImageUrl)

            firestore.collection(FirestorePaths.Products.collectionPath())
                .document(id)
                .set(dto)
                .await()

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun deleteCourse(productId: String, imageUrl: List<String>): Resource<Unit> {
        return try {
            imageUrl.forEach { url ->
                if (url.isNotBlank()) {
                    runCatching {
                        val imageRef = storage.getReferenceFromUrl(url)
                        imageRef.delete().await()
                    }
                }
            }

            firestore.collection(FirestorePaths.Products.collectionPath())
                .document(productId)
                .delete()
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun updateProduct(product: CreateProductModel): Resource<Unit> {
        return try {
            val newImages = uploadProductsImages(productId = product.id, images = product.images)
            val finalImages = product.remoteImages + newImages
            val dto = product.toDto(id = product.id, images = finalImages)

            firestore.collection(FirestorePaths.Products.collectionPath())
                .document(product.id)
                .set(dto)
                .await()

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getProductById(productId: String): Resource<ProductDetail> {
        return try {
            val snapshot = firestore
                .collection(FirestorePaths.Products.collectionPath())
                .document(productId)
                .get()
                .await()
            if (!snapshot.exists()) {
                return Resource.Error(
                    errorMapper.map(Exception("Producto no encontrado"))
                )
            }
            val course = snapshot
                .toObject(CreateProductDto::class.java)
                ?.copy(id = snapshot.id)
            if (course != null) {
                Resource.Success(course.toDetail())
            } else {
                Resource.Error(
                    errorMapper.map(Exception("Error al convertir producto"))
                )
            }

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getProductsByIds(ids: List<String>): Resource<List<ProductItem>> {
        return try {
            val result = mutableListOf<ProductItem>()

            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore.collection(FirestorePaths.Products.collectionPath())
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ProductItemDto::class.java)
                        ?.copy(id = doc.id)
                        ?.toDomain()
                }
            }

            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override fun getCategories(): Flow<Resource<List<CategoryModel>>> = callbackFlow {
        val listener = firestore
            .collection(FirestorePaths.Products.COLLECTION_PRODUCTS)
            .document(FirestorePaths.Products.DOCUMENT)
            .collection(FirestorePaths.Products.COLLECTION_CATEGORIES)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Resource.Error(errorMapper.map(error)))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val courses = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(CategoryModel::class.java)?.copy(id = doc.id)
                    }

                    trySend(Resource.Success(courses))
                }
            }

        awaitClose { listener.remove() }
    }

    override fun getAllProducts(): Flow<Resource<List<ProductItem>>> = callbackFlow {
        val listener = firestore
            .collection(FirestorePaths.Products.COLLECTION_PRODUCTS)
            .document(FirestorePaths.Products.DOCUMENT)
            .collection(FirestorePaths.Products.COLLECTION_PRODUCTS_ITEMS)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Resource.Error(errorMapper.map(error)))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ProductItemDto::class.java)
                            ?.copy(id = doc.id)
                            ?.toDomain()
                    }

                    trySend(Resource.Success(products))
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun decrementStock(items: List<CartItem>): Resource<Unit> {
        return try {
            if (items.isEmpty()) return Resource.Success(Unit)

            val col = firestore.collection(FirestorePaths.Products.collectionPath())

            firestore.runTransaction { transaction ->
                // Firestore exige realizar todas las lecturas antes de las escrituras
                val reads = items.map { item ->
                    item to transaction.get(col.document(item.productId))
                }
                reads.forEach { (item, snapshot) ->
                    if (!snapshot.exists()) return@forEach
                    // null = producto sin stock gestionado → no se toca
                    val current = snapshot.getLong(FirestorePaths.Products.STOCK)
                        ?: return@forEach
                    val newStock = (current - item.quantity).coerceAtLeast(0)
                    transaction.update(
                        col.document(item.productId),
                        FirestorePaths.Products.STOCK,
                        newStock
                    )
                }
                null
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    private suspend fun uploadProductsImages(
        productId: String,
        images: List<Uri>
    ): List<String> = coroutineScope {
        images.mapIndexed { index, imageUri ->
            async {
                val reference =
                    storage.reference.child("${StoragePaths.Products.productFolder(productId)}/image_$index.jpg")
                reference.putFile(imageUri).await()
                reference.downloadUrl.await().toString()
            }
        }.awaitAll()
    }
}