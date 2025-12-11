package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.network.CourseRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CourseRequestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CourseRequestRepository {

    private val requestsRef = firestore.collection("course_requests")

    override suspend fun sendRequest(request: CourseRequest): Resource<Boolean> {
        return try {
            val doc = requestsRef.document()
            val newRequest = request.copy(requestId = doc.id)

            doc.set(newRequest).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al enviar solicitud")
        }
    }

    override suspend fun getRequestsByStatus(status: String): Resource<List<CourseRequest>> {
        return try {
            val snapshot = requestsRef
                .whereEqualTo("status", status)
                .get()
                .await()

            val list = snapshot.toObjects(CourseRequest::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener solicitudes")
        }
    }

    override suspend fun approveRequest(requestId: String): Resource<Boolean> {
        return updateStatus(requestId, "aceptado")
    }

    override suspend fun rejectRequest(requestId: String): Resource<Boolean> {
        return try {
            val requestSnap = firestore.collection("course_requests")
                .document(requestId)
                .get()
                .await()

            if (!requestSnap.exists()) {
                return Resource.Error("La solicitud no existe")
            }

            val userId = requestSnap.getString("userId") ?: return Resource.Error("userId no encontrado")
            val courseId = requestSnap.getString("courseId") ?: return Resource.Error("courseId no encontrado")

            firestore.collection("course_requests")
                .document(requestId)
                .delete()
                .await()

            firestore.collection("users")
                .document(userId)
                .collection("cursos")
                .document(courseId)
                .update("status", "solicitar")
                .await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al rechazar solicitud")
        }
    }

    private suspend fun updateStatus(requestId: String, newStatus: String): Resource<Boolean> {
        return try {
            requestsRef.document(requestId)
                .update("status", newStatus)
                .await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar estado")
        }
    }
}