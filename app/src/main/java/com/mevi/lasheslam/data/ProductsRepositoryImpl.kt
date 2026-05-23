package com.mevi.lasheslam.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.data.constants.StoragePaths
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.repository.ProductsRepository
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CreateProductDto
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.network.ProductItemDto
import com.mevi.lasheslam.network.toDomain
import com.mevi.lasheslam.network.toDto
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

    override suspend fun getProductById(productId: String): Resource<CreateProductDto> {
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
                Resource.Success(course)
            } else {
                Resource.Error(
                    errorMapper.map(Exception("Error al convertir producto"))
                )
            }

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