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
            val requestRef = firestore.collection("course_requests")
                .document(requestId)

            val snapshot = requestRef.get().await()

            if (!snapshot.exists()) {
                return Resource.Error("La solicitud no existe")
            }

            val data = snapshot.data ?: return Resource.Error("La solicitud está vacía")

            val userId = data["userId"] as? String
                ?: return Resource.Error("userId no encontrado")
            val courseId = data["courseId"] as? String
                ?: return Resource.Error("courseId no encontrado")

            // 2. Crear curso dentro del usuario
            val cursoData = mapOf(
                "courseId" to courseId,
                "courseName" to (data["courseName"] as? String ?: ""),
                "date" to (data["date"] as? String ?: ""),
                "schedule" to (data["schedule"] as? String ?: ""),
                "status" to "aceptado",
                "requestId" to requestId,
                "notification" to "notCreated",
                "timestamp" to (data["timestamp"] ?: System.currentTimeMillis())
            )

            firestore.collection("users")
                .document(userId)
                .collection("cursos")
                .document(courseId)
                .set(cursoData)
                .await()

            // 3. Obtener foto del usuario
            val userSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val userPhoto = userSnapshot.getString("userPhoto")

            // 4. Crear documento padre del curso
            val cursoRef = firestore.collection("alumnos_inscritos")
                .document(courseId)

            cursoRef.set(mapOf("courseId" to courseId)).await()

            // 5. Crear alumno inscrito
            val alumnoData = data.toMutableMap()
            if (!userPhoto.isNullOrEmpty()) {
                alumnoData["userPhoto"] = userPhoto
            }

            cursoRef
                .collection("inscritos")
                .document(requestId)
                .set(alumnoData)
                .await()

            // 6. ELIMINAR REQUEST (FINAL)
            requestRef.delete().await()

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