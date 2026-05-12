package com.mevi.lasheslam.ui.services

import android.net.Uri

data class ServicesFormState(
    val titulo: String = "",
    val subtitulo: String = "",
    val precio: Double = 0.0,
    val image: Uri? = null,
    val duracion: Double = 0.0,
    val category: String = ""
)
