package com.mevi.lasheslam.domain.model

/** Estado de una reservación de servicio. La UI depende de este tipo de dominio, no de Firestore. */
enum class ReservationStatus(val value: String) {
    /** Reserva creada; el usuario aún debe enviar el comprobante del anticipo. */
    PENDING_DEPOSIT("pendiente_anticipo"),
    PENDING("pendiente"),
    SCHEDULED("agendado"),
    CANCELLED("cancelado"),
    ARCHIVED("archivado");

    companion object {
        fun fromValue(value: String?): ReservationStatus? =
            entries.firstOrNull { it.value == value }
    }
}
