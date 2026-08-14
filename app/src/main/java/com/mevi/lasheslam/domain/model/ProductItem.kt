package com.mevi.lasheslam.domain.model

data class ProductItem(
    val id: String,
    val actualPrice: Double,
    val bestSelling: Boolean,
    val category: String,
    val description: String,
    val characteristics: String,
    val images: List<String> = emptyList(),
    val title: String,
    val price: Double,
    // null = producto sin stock gestionado (productos antiguos) → sigue disponible
    val stock: Int? = null
)
