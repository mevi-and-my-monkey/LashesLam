package com.mevi.lasheslam.domain.repository

/** Persiste qué reservas ya recibieron la notificación inmediata de "agendada". */
interface ReservationNotificationStore {
    fun hasWelcomed(reservationId: String): Boolean
    fun markWelcomed(reservationId: String)
}
