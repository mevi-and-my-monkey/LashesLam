package com.mevi.lasheslam.domain.model

data class CartItem(
    val productId: String = "",
    val title: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)
