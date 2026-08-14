package com.mevi.lasheslam.data.dto

import com.mevi.lasheslam.domain.model.ProductItem
import com.google.firebase.firestore.PropertyName

data class ProductItemDto(
    val id: String = "",
    @get:PropertyName("actual_price")
    @set:PropertyName("actual_price")
    var actualPrice: Double = 0.0,
    @get:PropertyName("best_selling")
    @set:PropertyName("best_selling")
    var bestSelling: Boolean = false,
    val category: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val title: String = "",
    val price: Double? = 0.0,
    val characteristics: String = "",
    val stock: Int? = null
)

fun ProductItemDto.toDomain(): ProductItem {
    return ProductItem(
        id = id,
        actualPrice = actualPrice,
        bestSelling = bestSelling,
        category = category,
        description = description,
        images = images,
        title = title,
        price = price ?: 0.0,
        characteristics = characteristics,
        stock = stock
    )
}