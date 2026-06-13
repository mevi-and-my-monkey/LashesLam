package com.mevi.lasheslam.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guarda de forma persistente qué reservas ya recibieron la notificación
 * inmediata de "agendada", para no repetirla en cada arranque de la app.
 * (El usuario no puede escribir en la reserva en Firestore, por eso es local.)
 */
@Singleton
class ReservationNotificationStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs =
        context.getSharedPreferences("reservation_notifications", Context.MODE_PRIVATE)

    fun hasWelcomed(reservationId: String): Boolean =
        prefs.getBoolean(KEY_PREFIX + reservationId, false)

    fun markWelcomed(reservationId: String) {
        prefs.edit().putBoolean(KEY_PREFIX + reservationId, true).apply()
    }

    companion object {
        private const val KEY_PREFIX = "welcomed_"
    }
}
