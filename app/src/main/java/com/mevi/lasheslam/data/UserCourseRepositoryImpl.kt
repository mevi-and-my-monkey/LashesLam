package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
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

        val listener = firestore.collection("users")
            .document(userId)
            .collection("cursos")
            .whereEqualTo("status", "aceptado")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null) return@addSnapshotListener

                val courses = snapshot.documents.mapNotNull { doc ->
                    UserCourse(
                        id = doc.id,
                        name = doc.getString("courseName") ?: return@mapNotNull null,
                        date = doc.getString("date") ?: return@mapNotNull null,
                        schedule = doc.getString("schedule") ?: return@mapNotNull null,
                        notification = doc.getString("notification") ?: "notCreated"
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
        firestore.collection("users")
            .document(userId)
            .collection("cursos")
            .document(courseId)
            .update("notification", "created")
            .await()
    }
}