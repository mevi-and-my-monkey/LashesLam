package com.mevi.lasheslam.domain.model

data class ServiceReservation(
    val reservationId: String = "",
    val reservationNumber: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val durationLabel: String = "",
    val price: Double = 0.0,
    // Monto de anticipo solicitado para esta reserva (snapshot al reservar)
    val deposit: Double = 0.0,
    val userId: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    // Teléfono del cliente (para que el admin pueda contactarlo por WhatsApp)
    val phoneUser: String = "",
    // Fecha en formato ISO yyyy-MM-dd para poder consultar por igualdad
    val date: String = "",
    // Etiqueta legible, ej. "Vie 19 Jun"
    val dateLabel: String = "",
    // Horario en formato HH:mm
    val time: String = "",
    val status: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
