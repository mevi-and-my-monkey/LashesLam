package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.ServicesRepository
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.network.ServiceItemDto
import com.mevi.lasheslam.network.toDomain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ServicesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : ServicesRepository {

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
}