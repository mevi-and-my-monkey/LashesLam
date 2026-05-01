package com.mevi.lasheslam.data

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.network.CourseItemDto
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.toDomain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.chunked

class CoursesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : CoursesRepository {

    override suspend fun getCoursesByIds(
        ids: List<String>
    ): Resource<List<CoursesItem>> {
        return try {
            val result = mutableListOf<CoursesItem>()

            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore
                    .collection(FirestorePaths.Courses.COLLECTION)
                    .document(FirestorePaths.Courses.DOCUMENT)
                    .collection(FirestorePaths.Courses.COLLECTION_ITEMS)
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                result += snapshot.documents.mapNotNull { doc ->
                    doc.toObject(CourseItemDto::class.java)
                        ?.copy(id = doc.id)
                        ?.toDomain()
                }
            }

            Resource.Success(result)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override fun getAllCourses(): Flow<Resource<List<CoursesItem>>> = callbackFlow {
        val listener = firestore
            .collection(FirestorePaths.Courses.COLLECTION)
            .document(FirestorePaths.Courses.DOCUMENT)
            .collection(FirestorePaths.Courses.COLLECTION_ITEMS)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Resource.Error(errorMapper.map(error)))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val courses = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(CourseItemDto::class.java)
                            ?.copy(id = doc.id)
                            ?.toDomain()
                    }

                    trySend(Resource.Success(courses))
                }
            }

        awaitClose { listener.remove() }
    }
}