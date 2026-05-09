package com.mevi.lasheslam.domain.model

import android.net.Uri

data class CreateCourseModel(
    val titulo: String,
    val descripcion: String,
    val horaInicio: String,
    val horaFin: String,
    val fecha: String,
    val costo: String,
    val apartado: String,
    val instructora: String,
    val instructoraDesc: String,
    val temarios: List<String>,
    val imageUri: Uri?,
    val instructorImageUri: Uri?,
    val ubicacionNombre: String?,
    val lat: Double?,
    val lng: Double?,
    val banner: Int
)
