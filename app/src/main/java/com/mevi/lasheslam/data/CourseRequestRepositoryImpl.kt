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
        return try {

            // 1. Obtener data del request original
            val snapshot = firestore.collection("course_requests")
                .document(requestId)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Resource.Error("La solicitud no existe")
            }

            val data = snapshot.data ?: return Resource.Error("La solicitud está vacía")

            val userId = data["userId"] as? String
                ?: return Resource.Error("userId no encontrado")
            val courseId = data["courseId"] as? String
                ?: return Resource.Error("courseId no encontrado")

            // 2. Actualizar estado del request
            firestore.collection("course_requests")
                .document(requestId)
                .update("status", "aceptado")
                .await()

            // 3. Actualizar estado del curso dentro del usuario
            firestore.collection("users")
                .document(userId)
                .collection("cursos")
                .document(courseId)
                .update("status", "aceptado")
                .await()

            val userSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val userPhoto = userSnapshot.getString("userPhoto")

            // 4. Crear documento padre para el curso (IMPORTANTE)
            val cursoRef = firestore.collection("alumnos_inscritos")
                .document(courseId)

            cursoRef.set(mapOf("courseId" to courseId)).await()

            val alumnoData = data.toMutableMap()

            if (!userPhoto.isNullOrEmpty()) {
                alumnoData["userPhoto"] = userPhoto
            }

            // 5. Crear alumno inscrito
            cursoRef
                .collection("inscritos")
                .document(requestId)
                .set(alumnoData)
                .await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al aprobar solicitud")
        }
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
}