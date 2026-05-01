package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.ProductsRepository
import com.mevi.lasheslam.network.CategoryModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : ProductsRepository {

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
}