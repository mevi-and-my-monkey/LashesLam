package com.mevi.lasheslam.ui.courses

import android.net.Uri

data class CourseFormState(
    val titulo: String = "",
    val descripcion: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val fecha: String = "",
    val costo: String = "",
    val apartado: String = "",
    val instructora: String = "",
    val instructoraDesc: String = "",
    val temarios: List<String> = List(5) { "" },
    val imageUri: Uri? = null,
    val instructorImageUri: Uri? = null,
)