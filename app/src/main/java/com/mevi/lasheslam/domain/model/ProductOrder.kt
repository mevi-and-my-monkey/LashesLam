package com.mevi.lasheslam.domain.model

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
    // Forma de entrega: "recoger" o "domicilio" (ver DeliveryType)
    val deliveryType: String = "",
    // Domicilio de envío (snapshot). Vacío cuando es recoger en tienda.
    val address: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
