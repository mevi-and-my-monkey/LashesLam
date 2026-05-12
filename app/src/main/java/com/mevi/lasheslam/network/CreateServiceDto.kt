package com.mevi.lasheslam.network

import com.mevi.lasheslam.domain.model.CreateServiceModel

data class CreateServiceDto(
    val duration: Double = 0.0,
    val category: String = "",
    val subtitle: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val image: String = "",
)

fun CreateServiceModel.toDto(
    imageUrl: String,
): CreateServiceDto {
    return CreateServiceDto(
        duration = duration,
        category = category,
        subtitle = subtitle,
        price = price,
        title = title,
        image = imageUrl,
    )
}