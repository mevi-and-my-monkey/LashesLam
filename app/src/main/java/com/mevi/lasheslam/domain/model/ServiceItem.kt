package com.mevi.lasheslam.domain.model

data class ServiceItem(
    val id: String,
    val duration: Double,
    val image: String,
    val price: Double,
    val title: String,
    val subtitle: String,
    val category: String,
    val description: String = "",
    val includes: List<String> = emptyList(),
    val deposit: Double = 0.0,
    )