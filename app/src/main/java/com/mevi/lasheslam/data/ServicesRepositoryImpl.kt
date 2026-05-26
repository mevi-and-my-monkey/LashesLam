package com.mevi.lasheslam.data

import android.net.Uri
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.data.constants.StoragePaths
import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.domain.repository.ServicesRepository
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CreateServiceDto
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.network.ProductItemDto
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.network.ServiceItemDto
import com.mevi.lasheslam.network.toDomain
import com.mevi.lasheslam.network.toDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ServicesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val errorMapper: ErrorMapper
) : ServicesRepository {

    override suspend fun createService(service: CreateServiceModel): Resource<Unit> {
        return try {
            val id = UUID.randomUUID().toString()
            val serviceImg = uploadServiceImage(serviceId = id, imageUri = service.image!!)
            val dto = service.toDto(id = id, imageUrl = serviceImg)

            firestore.collection(FirestorePaths.Services.collectionPath())
                .document(id)
                .set(dto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }

    }

    override suspend fun getServiceId(serviceID: String): Resource<CreateServiceDto> {
        return try {
            val snapshot = firestore
                .collection(FirestorePaths.Services.collectionPath())
                .document(serviceID)
                .get()
                .await()
            if (!snapshot.exists()) {
                return Resource.Error(
                    errorMapper.map(Exception("Servicio no encontrado"))
                )
            }
            val course = snapshot
                .toObject(CreateServiceDto::class.java)
                ?.copy(id = snapshot.id)
            if (course != null) {
                Resource.Success(course)
            } else {
                Resource.Error(
                    errorMapper.map(Exception("Error al convertir servicio"))
                )
            }

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getServicesByIds(ids: List<String>): Resource<List<ServiceItem>> {
        return try {
            val result = mutableListOf<ServiceItem>()

            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore.collection(FirestorePaths.Services.collectionPath())
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ServiceItemDto::class.java)
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
            .collection(FirestorePaths.Services.COLLECTION_SERVICES)
            .document(FirestorePaths.Services.DOCUMENT)
            .collection(FirestorePaths.Services.COLLECTION_CATEGORIES)
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

    override fun getAllServices(): Flow<Resource<List<ServiceItem>>> = callbackFlow {
        val listener = firestore
            .collection(FirestorePaths.Services.COLLECTION_SERVICES)
            .document(FirestorePaths.Services.DOCUMENT)
            .collection(FirestorePaths.Services.COLLECTION_SERVICES_ITEMS)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Resource.Error(errorMapper.map(error)))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ServiceItemDto::class.java)
                            ?.copy(id = doc.id)
                            ?.toDomain()
                    }

                    trySend(Resource.Success(products))
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun deleteService(serviceId: String, imageUrl: String): Resource<Unit> {
        return try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
            firestore.collection(FirestorePaths.Services.collectionPath())
                .document(serviceId)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun updateService(service: CreateServiceModel): Resource<Unit> {
        return try {
            val serviceImageUrl = if (service.image != null) {
                uploadServiceImage(serviceId = service.id, imageUri = service.image)
            } else {
                service.currentImageUrl
            }

            val dto = service.toDto(
                imageUrl = serviceImageUrl,
                id = service.id
            )

            firestore.collection(FirestorePaths.Services.collectionPath())
                .document(service.id)
                .set(dto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    private suspend fun uploadServiceImage(serviceId: String, imageUri: Uri): String {
        val reference = storage.reference.child(
            StoragePaths.Services.serviceImage(serviceId)
        )
        reference.putFile(imageUri).await()
        return reference.downloadUrl.await().toString()
    }
}