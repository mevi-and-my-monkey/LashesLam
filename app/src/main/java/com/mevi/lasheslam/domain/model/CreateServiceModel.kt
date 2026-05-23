package com.mevi.lasheslam.domain.model

import android.net.Uri

data class CreateServiceModel(
    val id: String,
    val duration: Double,
    val category: String,
    val subtitle: String,
    val price: Double,
    val title: String,
    val currentImageUrl: String,
    val image: Uri?,
)
