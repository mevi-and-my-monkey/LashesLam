package com.mevi.lasheslam.network

data class ProductOrder(
    val orderId: String = "",
    val orderNumber: String = "",
    val userId: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val status: String = "",
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shipping: Double = 0.0,
    val total: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
