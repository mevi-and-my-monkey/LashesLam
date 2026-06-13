package com.mevi.lasheslam.ui.profile.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.usecase.GetServicesUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetReservationsByStatusUseCase
import com.mevi.lasheslam.domain.usecase.booking.SaveAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.UpdateReservationStatusUseCase
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.BookingSlot
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.network.ServiceReservation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

enum class ReservationFilter(val label: String) {
    TODOS("Todos"),
    PENDIENTE("Pendiente"),
    AGENDADO("Agendado"),
    CANCELADO("Cancelado"),
    ARCHIVADO("Archivado")
}

/** Fecha candidata para configurar disponibilidad (próximos días). */
data class AvailabilityDay(
    val isoDate: String,
    val dayLabel: String,
    val dayNumber: String,
    val monthLabel: String
)

@HiltViewModel
class AdminReservationsViewModel @Inject constructor(
    private val getReservationsByStatusUseCase: GetReservationsByStatusUseCase,
    private val updateReservationStatusUseCase: UpdateReservationStatusUseCase,
    private val getAvailabilityUseCase: GetAvailabilityUseCase,
    private val saveAvailabilityUseCase: SaveAvailabilityUseCase,
    private val getServicesUseCase: GetServicesUseCase
) : ViewModel() {

    companion object {
        private const val DAYS_AHEAD = 60
        private val DAY_LABELS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        private val MONTH_LABELS = listOf(
            "ENE", "FEB", "MAR", "ABR", "MAY", "JUN",
            "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"
        )
    }

    var reservations by mutableStateOf<List<ServiceReservation>>(emptyList())
        private set

    var filter by mutableStateOf(ReservationFilter.TODOS)
        private set

    var pendingCount by mutableIntStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // ---- Disponibilidad (por servicio) ----
    var services by mutableStateOf<List<ServiceItem>>(emptyList())
        private set

    var selectedServiceId by mutableStateOf<String?>(null)
        private set

    var schedule by mutableStateOf<Map<String, List<BookingSlot>>>(emptyMap())
        private set

    var selectableDays by mutableStateOf<List<AvailabilityDay>>(emptyList())
        private set

    var selectedEditorDate by mutableStateOf<String?>(null)
        private set

    fun onFilterSelected(newFilter: ReservationFilter) {
        filter = newFilter
        loadReservations()
    }

    fun loadReservations() = viewModelScope.launch {
        isLoading = true

        val statuses = when (filter) {
            // "Todos" muestra pendiente, agendado y cancelado (sin archivadas)
            ReservationFilter.TODOS -> listOf(
                FirestorePaths.Booking.STATUS_PENDING,
                FirestorePaths.Booking.STATUS_SCHEDULED,
                FirestorePaths.Booking.STATUS_CANCELLED
            )

            ReservationFilter.PENDIENTE -> listOf(FirestorePaths.Booking.STATUS_PENDING)
            ReservationFilter.AGENDADO -> listOf(FirestorePaths.Booking.STATUS_SCHEDULED)
            ReservationFilter.CANCELADO -> listOf(FirestorePaths.Booking.STATUS_CANCELLED)
            ReservationFilter.ARCHIVADO -> listOf(FirestorePaths.Booking.STATUS_ARCHIVED)
        }

        when (val result = getReservationsByStatusUseCase(statuses)) {
            is Resource.Success -> {
                reservations = result.data
                if (statuses.contains(FirestorePaths.Booking.STATUS_PENDING)) {
                    pendingCount = result.data.count {
                        it.status == FirestorePaths.Booking.STATUS_PENDING
                    }
                }
            }

            is Resource.Error -> reservations = emptyList()
        }
        isLoading = false
    }

    fun acceptReservation(reservationId: String) =
        updateStatus(reservationId, FirestorePaths.Booking.STATUS_SCHEDULED)

    fun rejectReservation(reservationId: String) =
        updateStatus(reservationId, FirestorePaths.Booking.STATUS_CANCELLED)

    fun archiveReservation(reservationId: String) =
        updateStatus(reservationId, FirestorePaths.Booking.STATUS_ARCHIVED)

    private fun updateStatus(reservationId: String, status: String) = viewModelScope.launch {
        isLoading = true
        updateReservationStatusUseCase(reservationId, status)
        isLoading = false
        loadReservations()
    }

    // ---------- Disponibilidad ----------

    fun loadAvailability() = viewModelScope.launch {
        buildSelectableDays()
        if (selectedEditorDate == null) {
            selectedEditorDate = selectableDays.firstOrNull()?.isoDate
        }

        // Carga la lista de servicios y selecciona el primero por defecto
        val servicesResult = getServicesUseCase().first()
        if (servicesResult is Resource.Success) {
            services = servicesResult.data
            if (selectedServiceId == null) {
                selectedServiceId = services.firstOrNull()?.id
            }
            selectedServiceId?.let { loadScheduleFor(it) }
        }
    }

    fun selectService(serviceId: String) {
        selectedServiceId = serviceId
        loadScheduleFor(serviceId)
    }

    private fun loadScheduleFor(serviceId: String) = viewModelScope.launch {
        when (val result = getAvailabilityUseCase(serviceId)) {
            is Resource.Success -> schedule = result.data.schedule
            is Resource.Error -> schedule = emptyMap()
        }
    }

    private fun buildSelectableDays() {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val days = mutableListOf<AvailabilityDay>()

        repeat(DAYS_AHEAD) {
            val isoWeekDay = ((calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1
            days.add(
                AvailabilityDay(
                    isoDate = isoFormat.format(calendar.time),
                    dayLabel = DAY_LABELS[isoWeekDay - 1],
                    dayNumber = calendar.get(Calendar.DAY_OF_MONTH).toString(),
                    monthLabel = MONTH_LABELS[calendar.get(Calendar.MONTH)]
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        selectableDays = days
    }

    fun selectEditorDate(isoDate: String) {
        selectedEditorDate = isoDate
    }

    fun addSlot(time: String) {
        val date = selectedEditorDate ?: return
        val normalized = time.trim()
        if (!normalized.matches(Regex("^\\d{2}:\\d{2}$"))) return

        val current = schedule[date].orEmpty()
        if (current.any { it.time == normalized }) return

        val updated = (current + BookingSlot(time = normalized, occupied = false))
            .sortedBy { it.time }
        schedule = schedule.toMutableMap().apply { put(date, updated) }
    }

    fun removeSlot(time: String) {
        val date = selectedEditorDate ?: return
        val updated = schedule[date].orEmpty().filterNot { it.time == time }
        schedule = schedule.toMutableMap().apply {
            if (updated.isEmpty()) remove(date) else put(date, updated)
        }
    }

    fun toggleSlotOccupied(time: String) {
        val date = selectedEditorDate ?: return
        val updated = schedule[date].orEmpty().map {
            if (it.time == time) it.copy(occupied = !it.occupied) else it
        }
        schedule = schedule.toMutableMap().apply { put(date, updated) }
    }

    fun slotsForSelectedDate(): List<BookingSlot> =
        selectedEditorDate?.let { schedule[it].orEmpty() } ?: emptyList()

    fun dayHasSlots(isoDate: String): Boolean = schedule[isoDate]?.isNotEmpty() == true

    fun saveAvailability(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        val serviceId = selectedServiceId ?: run {
            onResult(false)
            return@launch
        }
        isLoading = true
        val result = saveAvailabilityUseCase(serviceId, BookingAvailability(schedule = schedule))
        isLoading = false
        onResult(result is Resource.Success)
    }
}
