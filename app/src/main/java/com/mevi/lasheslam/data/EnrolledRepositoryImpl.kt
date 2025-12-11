package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.EnrolledCourse
import com.mevi.lasheslam.network.EnrolledStudent
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class EnrolledRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val inscritosRef = firestore.collection("alumnos_inscritos")
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    /** Obtiene cursos ordenados por fecha más reciente */
    suspend fun getCourses(): Resource<List<EnrolledCourse>> {
        return try {
            val snap = inscritosRef.get().await()
            val cursos = mutableListOf<EnrolledCourse>()

            for (doc in snap.documents) {
                val courseId = doc.id

                val first = inscritosRef.document(courseId)
                    .collection("inscritos")
                    .limit(1)
                    .get()
                    .await()

                if (first.isEmpty) continue

                val data = first.documents.first()

                val courseName = data.getString("courseName") ?: "Curso"
                val dateStr = data.getString("date") ?: "01/01/2000"

                val inscritosCount = inscritosRef.document(courseId)
                    .collection("inscritos")
                    .get()
                    .await()
                    .size()

                cursos.add(
                    EnrolledCourse(
                        courseId = courseId,
                        courseName = courseName,
                        fecha = dateStr,
                        inscritosCount = inscritosCount
                    )
                )
            }

            val ordenados = cursos.sortedByDescending {
                LocalDate.parse(it.fecha, formatter)
            }

            Resource.Success(ordenados)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener cursos inscritos")
        }
    }

    /** Obtiene alumnos de un curso */
    suspend fun getStudents(courseId: String): Resource<List<EnrolledStudent>> {
        return try {
            val snap = inscritosRef.document(courseId)
                .collection("inscritos")
                .get()
                .await()

            val list = snap.documents.mapNotNull {
                it.toObject(EnrolledStudent::class.java)
            }

            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener alumnos del curso")
        }
    }
}