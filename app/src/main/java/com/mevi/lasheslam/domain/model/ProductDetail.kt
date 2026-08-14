package com.mevi.lasheslam.domain.model

/** Modelo de dominio con el detalle de un producto (lectura/edición). */
data class ProductDetail(
    val id: String = "",
    val actulPrice: Double = 0.0,
    val bestSelling: Boolean = false,
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val characteristics: String = "",
    val images: List<String> = emptyList(),
    val stock: Int? = null,
)
