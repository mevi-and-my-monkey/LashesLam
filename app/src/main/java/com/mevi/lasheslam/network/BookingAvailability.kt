package com.mevi.lasheslam.network

/**
 * Un horario ofrecido en una fecha concreta.
 * occupied = true lo marca el administrador manualmente para bloquearlo.
 */
data class BookingSlot(
    val time: String = "",
    val occupied: Boolean = false
)

/**
 * Disponibilidad de citas que define el administrador.
 * schedule: mapa de fecha ISO (yyyy-MM-dd) a la lista de horarios de ESE día.
 * Cada día tiene horarios independientes.
 */
data class BookingAvailability(
    val schedule: Map<String, List<BookingSlot>> = emptyMap()
)
