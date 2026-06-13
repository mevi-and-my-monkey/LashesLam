package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.BookingRepository
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.BookingSlot
import com.mevi.lasheslam.network.ServiceReservation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val errorMapper: ErrorMapper
) : BookingRepository {

    private fun availabilityRef(serviceId: String) = firestore
        .collection(FirestorePaths.Booking.AVAILABILITY_COLLECTION)
        .document(serviceId)

    private val reservationsRef = firestore
        .collection(FirestorePaths.Booking.RESERVATIONS_COLLECTION)

    private val activeStatuses = listOf(
        FirestorePaths.Booking.STATUS_PENDING,
        FirestorePaths.Booking.STATUS_SCHEDULED
    )

    override suspend fun getAvailability(serviceId: String): Resource<BookingAvailability> {
        return try {
            val snapshot = availabilityRef(serviceId).get().await()

            @Suppress("UNCHECKED_CAST")
            val rawSchedule = snapshot.get(FirestorePaths.Booking.SCHEDULE) as? Map<String, *>

            val schedule = rawSchedule
                ?.mapValues { (_, value) ->
                    (value as? List<*>).orEmpty().mapNotNull { entry ->
                        val map = entry as? Map<*, *> ?: return@mapNotNull null
                        val time = map[FirestorePaths.Booking.SLOT_TIME] as? String
                            ?: return@mapNotNull null
                        val occupied = map[FirestorePaths.Booking.SLOT_OCCUPIED] as? Boolean ?: false
                        BookingSlot(time = time, occupied = occupied)
                    }.sortedBy { it.time }
                }
                .orEmpty()

            Resource.Success(BookingAvailability(schedule = schedule))
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun saveAvailability(
        serviceId: String,
        availability: BookingAvailability
    ): Resource<Boolean> {
        return try {
            val scheduleMap = availability.schedule.mapValues { (_, slots) ->
                slots.map { slot ->
                    mapOf(
                        FirestorePaths.Booking.SLOT_TIME to slot.time,
                        FirestorePaths.Booking.SLOT_OCCUPIED to slot.occupied
                    )
                }
            }

            availabilityRef(serviceId).set(
                mapOf(FirestorePaths.Booking.SCHEDULE to scheduleMap)
            ).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getTakenSlots(serviceId: String, date: String): Resource<List<String>> {
        return try {
            // Consulta por un solo campo (date) para no requerir índice compuesto;
            // se filtra serviceId y status en memoria. Una reserva pendiente o
            // agendada ocupa el horario; cancelada/archivada lo libera.
            val snapshot = reservationsRef
                .whereEqualTo(FirestorePaths.Booking.DATE, date)
                .get()
                .await()

            val taken = snapshot.documents
                .mapNotNull { it.toObject(ServiceReservation::class.java) }
                .filter { it.serviceId == serviceId && it.status in activeStatuses }
                .map { it.time }

            Resource.Success(taken)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun createReservation(
        reservation: ServiceReservation
    ): Resource<ServiceReservation> {
        return try {
            // El userId se toma de FirebaseAuth para garantizar que coincide con
            // request.auth.uid en las reglas de Firestore
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error(
                    errorMapper.map(Exception("Sesión no válida"))
                )

            val doc = reservationsRef.document()
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val newReservation = reservation.copy(
                reservationId = doc.id,
                reservationNumber = "LL-$year-${doc.id.takeLast(4).uppercase()}",
                userId = uid,
                status = FirestorePaths.Booking.STATUS_PENDING
            )

            doc.set(newReservation).await()

            Resource.Success(newReservation)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override fun observeScheduledReservations(userId: String): Flow<List<ServiceReservation>> =
        callbackFlow {
            val listener = reservationsRef
                .whereEqualTo(FirestorePaths.Booking.USER_ID, userId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot == null) return@addSnapshotListener
                    val scheduled = snapshot.documents
                        .mapNotNull { it.toObject(ServiceReservation::class.java) }
                        .filter { it.status == FirestorePaths.Booking.STATUS_SCHEDULED }
                    trySend(scheduled)
                }

            awaitClose { listener.remove() }
        }

    override suspend fun getReservationsByUser(
        userId: String
    ): Resource<List<ServiceReservation>> {
        return try {
            val snapshot = reservationsRef
                .whereEqualTo(FirestorePaths.Booking.USER_ID, userId)
                .get()
                .await()

            val list = snapshot.documents
                .mapNotNull { it.toObject(ServiceReservation::class.java) }
                .sortedByDescending { it.timestamp }

            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getReservationsByStatus(
        statuses: List<String>
    ): Resource<List<ServiceReservation>> {
        return try {
            val snapshot = reservationsRef
                .whereIn(FirestorePaths.Booking.STATUS, statuses)
                .get()
                .await()

            val list = snapshot.documents
                .mapNotNull { it.toObject(ServiceReservation::class.java) }
                .sortedByDescending { it.timestamp }

            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun updateStatus(
        reservationId: String,
        status: String
    ): Resource<Boolean> {
        return try {
            reservationsRef.document(reservationId)
                .update(FirestorePaths.Booking.STATUS, status)
                .await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }
}
