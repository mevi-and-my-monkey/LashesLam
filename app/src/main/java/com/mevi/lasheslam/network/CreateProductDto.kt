package com.mevi.lasheslam.network

import com.google.firebase.firestore.PropertyName
import com.mevi.lasheslam.domain.model.CreateProductModel

data class CreateProductDto(
    @get:PropertyName("actual_price")
    @set:PropertyName("actual_price")
    var actulPrice: Double = 0.0,
    @get:PropertyName("best_selling")
    @set:PropertyName("best_selling")
    var bestSelling: Boolean = false,
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val images: List<String> = emptyList()
)

fun CreateProductModel.toDto(
    images: List<String>
): CreateProductDto {
    return CreateProductDto(
        actulPrice = actulPrice,
        bestSelling = bestSelling,
        category = category,
        description = description,
        price = price,
        title = title,
        images = images
    )
}