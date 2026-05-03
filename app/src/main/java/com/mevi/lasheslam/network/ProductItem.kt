package com.mevi.lasheslam.network

data class ProductItem(
    val id: String,
    val actualPrice: Double,
    val bestSelling: Boolean,
    val category: String,
    val description: String,
    val images: List<String> = emptyList(),
    val title: String,
    val price: Double
)
