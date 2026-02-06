package com.mevi.lasheslam.data

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.network.ServiceItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CoursesRepository {

    override suspend fun getCoursesByIds(
        ids: List<String>
    ): Resource<List<ServiceItem>> {
        return try {
            val result = mutableListOf<ServiceItem>()

            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore
                    .collection("data")
                    .document("curse")
                    .collection("items")
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ServiceItem::class.java)?.copy(id = doc.id)
                }
            }

            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al cargar cursos")
        }
    }
}