package com.mevi.lasheslam.domain.usecase.booking

import android.os.Build
import android.util.Log
import com.mevi.lasheslam.data.local.NotificationCache
import com.mevi.lasheslam.data.local.ReservationNotificationStore
import com.mevi.lasheslam.domain.repository.NotificationScheduler
import com.mevi.lasheslam.network.ServiceReservation
import com.mevi.lasheslam.utils.date.CourseDateParser
import javax.inject.Inject

/**
 * Para cada reserva agendada del usuario programa recordatorios locales
 * (3, 2 y 1 día antes, y 2 horas antes). Idempotente: WorkManager reemplaza
 * el trabajo único por reserva, así que re-procesar en cada arranque no duplica.
 */
class HandleReservationNotificationsUseCase @Inject constructor(
    private val scheduler: NotificationScheduler,
    private val dateParser: CourseDateParser,
    private val cache: NotificationCache,
    private val store: ReservationNotificationStore
) {
    operator fun invoke(reservations: List<ServiceReservation>) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        reservations.forEach { reservation ->
            // Notificación inmediata "agendada", una sola vez por reserva
            if (!store.hasWelcomed(reservation.reservationId)) {
                scheduler.notifyNow(
                    title = "Cita agendada 🎉",
                    message = "Tu cita de ${reservation.serviceName} quedó agendada"
                )
                store.markWelcomed(reservation.reservationId)
            }

            // Evita reprogramar la misma reserva varias veces en la misma sesión
            if (!cache.shouldProcess("reservation-${reservation.reservationId}")) return@forEach

            val startDateTime = runCatching {
                dateParser.parseIso(reservation.date, reservation.time)
            }.getOrNull() ?: return@forEach

            try {
                scheduler.scheduleReservation(
                    reservationId = reservation.reservationId,
                    serviceName = reservation.serviceName,
                    startDateTime = startDateTime
                )
            } catch (e: Exception) {
                Log.e("Notifications", "Error scheduling reservation reminder", e)
            }
        }
    }
}
