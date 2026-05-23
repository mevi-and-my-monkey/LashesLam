package com.mevi.lasheslam.domain.model

import android.net.Uri

data class CreateProductModel(
    val id: String,
    val actulPrice: Double,
    val bestSelling: Boolean,
    val category: String,
    val description: String,
    val price: Double,
    val title: String,
    val characteristics: String,
    val images: List<Uri> = emptyList(),
    val remoteImages: List<String> = emptyList()
)
