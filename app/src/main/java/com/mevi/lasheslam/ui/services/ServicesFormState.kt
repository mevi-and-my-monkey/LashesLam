package com.mevi.lasheslam.ui.services

import android.net.Uri

data class ServicesFormState(
    val id: String = "",
    val titulo: String = "",
    val subtitulo: String = "",
    val precio: String = "",
    val image: Uri? = null,
    val duracion: String = "",
    val category: String = "",
    val remoteImage: String = ""
)
