package com.mevi.lasheslam.data.dto

import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.domain.model.ServiceDetail

data class CreateServiceDto(
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
        description = description,
        includes = includes,
        deposit = deposit,
    )
}

fun CreateServiceDto.toDetail(): ServiceDetail = ServiceDetail(
    id = id,
    duration = duration,
    category = category,
    subtitle = subtitle,
    price = price,
    title = title,
    image = image,
    description = description,
    includes = includes,
    deposit = deposit,
)