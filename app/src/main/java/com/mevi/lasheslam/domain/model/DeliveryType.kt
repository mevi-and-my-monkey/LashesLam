package com.mevi.lasheslam.domain.model

/** Forma de entrega de un pedido de productos. */
enum class DeliveryType(val value: String) {
    PICKUP("recoger"),
    DELIVERY("domicilio");

    companion object {
        fun fromValue(value: String?): DeliveryType? =
            entries.firstOrNull { it.value == value }
    }
}
