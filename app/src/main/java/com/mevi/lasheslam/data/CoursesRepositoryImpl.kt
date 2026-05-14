package com.mevi.lasheslam.data

import android.net.Uri
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.data.constants.StoragePaths
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.network.CourseItemDto
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.CreateCourseDto
import com.mevi.lasheslam.network.toDomain
import com.mevi.lasheslam.network.toDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.chunked

class CoursesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val errorMapper: ErrorMapper
) : CoursesRepository {

    override suspend fun getCoursesByIds(
        ids: List<String>
    ): Resource<List<CoursesItem>> {
        return try {
            val result = mutableListOf<CoursesItem>()

            ids.chunked(10).forEach { chunk ->
                val snapshot = firestore.collection(FirestorePaths.Courses.collectionPath())
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
        val listener = firestore.collection(FirestorePaths.Courses.collectionPath())
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

    override suspend fun getCourseById(
        courseId: String
    ): Resource<CreateCourseDto> {
        return try {
            val snapshot = firestore
                .collection(FirestorePaths.Courses.collectionPath())
                .document(courseId)
                .get()
                .await()
            if (!snapshot.exists()) {
                return Resource.Error(
                    errorMapper.map(Exception("Curso no encontrado"))
                )
            }
            val course = snapshot
                .toObject(CreateCourseDto::class.java)
                ?.copy(id = snapshot.id)
            if (course != null) {
                Resource.Success(course)
            } else {
                Resource.Error(
                    errorMapper.map(Exception("Error al convertir curso"))
                )
            }

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun createCourse(
        course: CreateCourseModel
    ): Resource<Unit> {
        return try {
            val id = UUID.randomUUID().toString()
            val courseImageUrl = uploadCourseImage(courseId = id, imageUri = course.imageUri!!)
            val instructorImageUrl =
                uploadInstructorImage(courseId = id, imageUri = course.instructorImageUri!!)
            val dto = course.toDto(
                id = id,
                imageUrl = courseImageUrl,
                instructorImageUrl = instructorImageUrl
            )

            firestore.collection(FirestorePaths.Courses.collectionPath())
                .document(id)
                .set(dto)
                .await()

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    private suspend fun uploadCourseImage(courseId: String, imageUri: Uri): String {
        val reference = storage.reference.child(
            StoragePaths.Courses.courseImage(courseId)
        )
        reference.putFile(imageUri).await()
        return reference.downloadUrl.await().toString()
    }

    private suspend fun uploadInstructorImage(courseId: String, imageUri: Uri): String {
        val reference = storage.reference.child(
            StoragePaths.Courses.instructorImage(courseId)
        )
        reference.putFile(imageUri).await()
        return reference.downloadUrl.await().toString()
    }

}