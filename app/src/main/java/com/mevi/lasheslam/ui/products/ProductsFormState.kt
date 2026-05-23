package com.mevi.lasheslam.ui.products

import android.net.Uri

data class ProductsFormState(
    val id: String = "",
    val precioActual: String = "",
    val masVendidos: Boolean = false,
    val category: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val titulo: String = "",
    val caracteristicas: String = "",
    val images: List<Uri> = emptyList(),
    val remoteImages: List<String> = emptyList()
)
