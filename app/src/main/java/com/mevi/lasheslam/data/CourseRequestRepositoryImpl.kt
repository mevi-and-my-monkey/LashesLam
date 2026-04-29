package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.network.CourseRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CourseRequestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : CourseRequestRepository {

    private val requestsRef = firestore.collection(FirestorePaths.Courses.COURSES_REQUESTS)

    override suspend fun sendRequest(request: CourseRequest): Resource<Boolean> {
        return try {
            val doc = requestsRef.document()
            val newRequest = request.copy(requestId = doc.id)

            doc.set(newRequest).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getRequestsByStatus(status: String): Resource<List<CourseRequest>> {
        return try {
            val snapshot = requestsRef
                .whereEqualTo(FirestorePaths.Courses.STATUS, status)
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { it.toObject(CourseRequest::class.java) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun approveRequest(requestId: String): Resource<Boolean> {
        return try {
            val requestRef = firestore
                .collection(FirestorePaths.Courses.COURSES_REQUESTS)
                .document(requestId)

            val snapshot = requestRef.get().await()

            if (!snapshot.exists()) {
                return Resource.Error(AppError.Unknown(Strings.Firestore.REQUEST_NOT_FOUND))
            }

            val data = snapshot.data
                ?: return Resource.Error(AppError.Unknown(Strings.Firestore.REQUEST_EMPTY))

            val userId = data[FirestorePaths.Users.USER_ID] as? String
                ?: return Resource.Error(AppError.Unknown(Strings.Firestore.USER_NOT_FOUND))

            val courseId = data[FirestorePaths.Courses.COURSE_ID] as? String
                ?: return Resource.Error(AppError.Unknown(Strings.Firestore.COURSE_NOT_FOUND))

            val userCourseRef = firestore
                .collection(FirestorePaths.Users.COLLECTION)
                .document(userId)
                .collection(FirestorePaths.Users.COURSE)
                .document(courseId)

            val courseParentRef = firestore
                .collection(FirestorePaths.Courses.STUDENTS_ENROLLED)
                .document(courseId)

            val enrolledRef = courseParentRef
                .collection(FirestorePaths.Courses.ENROLLED)
                .document(requestId)

            val cursoData = mapOf(
                FirestorePaths.Courses.COURSE_ID to courseId,
                FirestorePaths.Courses.COURSE_NAME to (data[FirestorePaths.Courses.COURSE_NAME] as? String ?: ""),
                FirestorePaths.Courses.DATE to (data[FirestorePaths.Courses.DATE] as? String ?: ""),
                FirestorePaths.Courses.SCHEDULE to (data[FirestorePaths.Courses.SCHEDULE] as? String ?: ""),
                FirestorePaths.Courses.STATUS to FirestorePaths.Courses.STATUS_ACCEPTED,
                FirestorePaths.Courses.REQUEST_ID to requestId,
                FirestorePaths.Courses.NOTIFICATION to FirestorePaths.Courses.NOTIFICATION_NOT_CREATED,
                FirestorePaths.Courses.TIMESTAMP to (data[FirestorePaths.Courses.TIMESTAMP] ?: System.currentTimeMillis())
            )

            val alumnoData = data.toMutableMap()

            val batch = firestore.batch()

            batch.set(userCourseRef, cursoData)
            batch.set(courseParentRef, mapOf(FirestorePaths.Courses.COURSE_ID to courseId))
            batch.set(enrolledRef, alumnoData)
            batch.delete(requestRef)

            batch.commit().await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun rejectRequest(requestId: String): Resource<Boolean> {
        return try {
            val requestRef = firestore
                .collection(FirestorePaths.Courses.COURSES_REQUESTS)
                .document(requestId)

            val snapshot = requestRef.get().await()

            if (!snapshot.exists()) {
                return Resource.Error(AppError.Unknown(Strings.Firestore.REQUEST_NOT_FOUND))
            }

            val userId = snapshot.getString(FirestorePaths.Users.USER_ID)
                ?: return Resource.Error(AppError.Unknown(Strings.Firestore.USER_NOT_FOUND))

            val courseId = snapshot.getString(FirestorePaths.Courses.COURSE_ID)
                ?: return Resource.Error(AppError.Unknown(Strings.Firestore.COURSE_NOT_FOUND))

            val userCourseRef = firestore
                .collection(FirestorePaths.Users.COLLECTION)
                .document(userId)
                .collection(FirestorePaths.Users.COURSE)
                .document(courseId)

            val batch = firestore.batch()

            batch.delete(requestRef)
            batch.update(
                userCourseRef,
                FirestorePaths.Courses.STATUS,
                FirestorePaths.Courses.REQUEST
            )

            batch.commit().await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }
}