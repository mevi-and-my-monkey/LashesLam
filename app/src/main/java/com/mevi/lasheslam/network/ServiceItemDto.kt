package com.mevi.lasheslam.network

data class ServiceItemDto(
    val id: String = "",
    val duration: Double? = 0.0,
    val image: String = "",
    val price: Double? = 0.0,
    val title: String = "",
    val subtitle: String = "",
    val category: String = "",
    )

fun ServiceItemDto.toDomain(): ServiceItem {
    return ServiceItem(
        id = id,
        duration = duration ?: 0.0,
        image = image,
        price = price ?: 0.0,
        title = title,
        subtitle = subtitle,
        category = category,
    )
}