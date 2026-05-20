package com.mevi.lasheslam.domain.model

import android.net.Uri

data class UpdateCourseModel(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val horaInicio: String,
    val horaFin: String,
    val fecha: String,
    val costo: String,
    val apartar: String,
    val instructora: String,
    val instructoraDesc: String,
    val temarios: List<String>,
    val currentImageUrl: String,
    val newImageUri: Uri?,
    val currentInstructorImageUrl: String,
    val newInstructorImageUri: Uri?,
    val ubicacionNombre: String?,
    val lat: Double?,
    val lng: Double?,
    val banner: Int
)
