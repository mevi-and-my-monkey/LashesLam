package com.mevi.lasheslam.data.dto

import com.mevi.lasheslam.domain.model.ServiceItem
data class ServiceItemDto(
    val id: String = "",
    val duration: Double? = 0.0,
    val image: String = "",
    val price: Double? = 0.0,
    val title: String = "",
    val subtitle: String = "",
    val category: String = "",
    val description: String = "",
    val includes: List<String> = emptyList(),
    val deposit: Double? = 0.0,
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
        description = description,
        includes = includes,
        deposit = deposit ?: 0.0,
    )
}