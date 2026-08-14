package com.mevi.lasheslam.domain.model

/** Modelo de dominio con el detalle de un curso (lectura/edición). */
data class CourseDetail(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val horaIncio: String = "",
    val horaFin: String = "",
    val fecha: String = "",
    val costo: String = "",
    val apartar: String = "",
    val instructora: String = "",
    val instructoraDesc: String = "",
    val temarios: List<String> = emptyList(),
    val imagen: String = "",
    val instructoraImage: String = "",
    val ubicacionNombre: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val banner: Int = 0,
)
