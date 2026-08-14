package com.mevi.lasheslam.domain.model

/** Modelo de dominio con el detalle de un servicio (lectura/edición). */
data class ServiceDetail(
    val id: String = "",
    val duration: Double = 0.0,
    val category: String = "",
    val subtitle: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val includes: List<String> = emptyList(),
    val deposit: Double = 0.0,
)
