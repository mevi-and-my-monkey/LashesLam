package com.mevi.lasheslam.network

import com.mevi.lasheslam.domain.model.CreateServiceModel

data class CreateServiceDto(
    val id: String = "",
    val duration: Double = 0.0,
    val category: String = "",
    val subtitle: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val image: String = "",
)

fun CreateServiceModel.toDto(
    imageUrl: String,
    id: String
): CreateServiceDto {
    return CreateServiceDto(
        id = id,
        duration = duration,
        category = category,
        subtitle = subtitle,
        price = price,
        title = title,
        image = imageUrl,
    )
}