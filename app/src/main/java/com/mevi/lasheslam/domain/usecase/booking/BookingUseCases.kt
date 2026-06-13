package com.mevi.lasheslam.domain.usecase.booking

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.BookingRepository
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.ServiceReservation
import javax.inject.Inject

class GetAvailabilityUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(serviceId: String): Resource<BookingAvailability> =
        repository.getAvailability(serviceId)
}

class SaveAvailabilityUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        serviceId: String,
        availability: BookingAvailability
    ): Resource<Boolean> = repository.saveAvailability(serviceId, availability)
}

class GetTakenSlotsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(serviceId: String, date: String): Resource<List<String>> =
        repository.getTakenSlots(serviceId, date)
}

class CreateReservationUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(reservation: ServiceReservation): Resource<ServiceReservation> =
        repository.createReservation(reservation)
}

class GetUserReservationsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(userId: String): Resource<List<ServiceReservation>> =
        repository.getReservationsByUser(userId)
}

class GetReservationsByStatusUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(statuses: List<String>): Resource<List<ServiceReservation>> =
        repository.getReservationsByStatus(statuses)
}

class UpdateReservationStatusUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(reservationId: String, status: String): Resource<Boolean> =
        repository.updateStatus(reservationId, status)
}
