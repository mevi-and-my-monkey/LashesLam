package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.ServiceReservation
import kotlinx.coroutines.flow.Flow

interface BookingRepository {

    /** Reservas del usuario en estado agendado, en tiempo real (para recordatorios) */
    fun observeScheduledReservations(userId: String): Flow<List<ServiceReservation>>

    suspend fun getAvailability(serviceId: String): Resource<BookingAvailability>

    suspend fun saveAvailability(
        serviceId: String,
        availability: BookingAvailability
    ): Resource<Boolean>

    /** Horarios ya reservados (pendientes o agendados) para un servicio y fecha */
    suspend fun getTakenSlots(serviceId: String, date: String): Resource<List<String>>

    suspend fun createReservation(reservation: ServiceReservation): Resource<ServiceReservation>

    suspend fun getReservationsByUser(userId: String): Resource<List<ServiceReservation>>

    suspend fun getReservationsByStatus(statuses: List<String>): Resource<List<ServiceReservation>>

    suspend fun updateStatus(reservationId: String, status: String): Resource<Boolean>
}
