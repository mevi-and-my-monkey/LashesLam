package com.mevi.lasheslam.domain.model

/** Estado de una orden de productos. */
enum class OrderStatus(val value: String) {
    PENDING("pendiente"),

    // Estados de envío a domicilio (el admin los va actualizando)
    PREPARING("preparando"),
    SHIPPED("enviado"),
    DELIVERED("entregado"),

    /** Pedido de recoger en tienda ya completado. */
    COMPLETED("finalizado"),
    ARCHIVED("archivado"),

    /** Órdenes creadas antes del cambio de "aceptado" a "finalizado". */
    LEGACY_ACCEPTED("aceptado");

    companion object {
        fun fromValue(value: String?): OrderStatus? =
            entries.firstOrNull { it.value == value }
    }
}
