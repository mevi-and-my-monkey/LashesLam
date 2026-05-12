package com.mevi.lasheslam.ui.products

import android.net.Uri

data class ProductsFormState(
    val precioActual: Double = 0.0,
    val masVendidos: Boolean = false,
    val category: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val titulo: String = "",
    val images: List<Uri> = emptyList()
)
