package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.model.UserCourse
import com.mevi.lasheslam.domain.repository.UserCourseRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserCourseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserCourseRepository {

    override fun observeAcceptedCourses(userId: String) = callbackFlow {

        val listener = firestore.collection(FirestorePaths.Users.COLLECTION)
            .document(userId)
            .collection(FirestorePaths.Users.COURSE)
            .whereEqualTo(FirestorePaths.Courses.STATUS, FirestorePaths.Courses.STATUS_ACCEPTED)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null) return@addSnapshotListener

                val courses = snapshot.documents.mapNotNull { doc ->
                    UserCourse(
                        id = doc.id,
                        name = doc.getString(FirestorePaths.Courses.COURSE_NAME)
                            ?: return@mapNotNull null,
                        date = doc.getString(FirestorePaths.Courses.DATE) ?: return@mapNotNull null,
                        schedule = doc.getString(FirestorePaths.Courses.SCHEDULE)
                            ?: return@mapNotNull null,
                        notification = doc.getString(FirestorePaths.Courses.NOTIFICATION)
                            ?: FirestorePaths.Courses.NOTIFICATION_NOT_CREATED
                    )
                }

                trySend(courses)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun markNotificationAsCreated(
        userId: String,
        courseId: String
    ) {
        firestore.collection(FirestorePaths.Users.COLLECTION)
            .document(userId)
            .collection(FirestorePaths.Users.COURSE)
            .document(courseId)
            .update(
                FirestorePaths.Courses.NOTIFICATION,
                FirestorePaths.Courses.NOTIFICATION_CREATED
            )
            .await()
    }
}