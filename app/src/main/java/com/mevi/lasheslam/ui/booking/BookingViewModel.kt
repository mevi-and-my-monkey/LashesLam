package com.mevi.lasheslam.ui.booking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.booking.CreateReservationUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetTakenSlotsUseCase
import com.mevi.lasheslam.domain.usecase.service.GetAServiceDetailUseCase
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.BookingSlot
import com.mevi.lasheslam.network.CreateServiceDto
import com.mevi.lasheslam.network.ServiceReservation
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.utils.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/** Día seleccionable en la pantalla de reserva */
data class BookingDay(
    val isoDate: String,      // 2026-06-19
    val dayLabel: String,     // Vie
    val dayNumber: String,    // 19
    val monthLabel: String    // JUNIO 2026
)

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getAServiceDetailUseCase: GetAServiceDetailUseCase,
    private val getAvailabilityUseCase: GetAvailabilityUseCase,
    private val getTakenSlotsUseCase: GetTakenSlotsUseCase,
    private val createReservationUseCase: CreateReservationUseCase
) : ViewModel() {

    companion object {
        private val DAY_LABELS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        private val MONTH_LABELS = listOf(
            "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO",
            "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"
        )
    }

    // Disponibilidad completa (fecha ISO -> horarios) y slots del día seleccionado
    private var schedule: Map<String, List<BookingSlot>> = emptyMap()
    private var daySlots: List<BookingSlot> = emptyList()

    var service by mutableStateOf<CreateServiceDto?>(null)
        private set

    var availableDays by mutableStateOf<List<BookingDay>>(emptyList())
        private set

    var slots by mutableStateOf<List<String>>(emptyList())
        private set

    var takenSlots by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedDate by mutableStateOf<BookingDay?>(null)
        private set

    var selectedTime by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var reservationPlaced by mutableStateOf<ServiceReservation?>(null)
        private set

    var showError by mutableStateOf(false)
        private set

    private var serviceId: String = ""

    fun load(serviceId: String) {
        if (service != null) return
        this.serviceId = serviceId
        viewModelScope.launch {
            isLoading = true

            when (val result = getAServiceDetailUseCase(serviceId)) {
                is Resource.Success -> service = result.data
                is Resource.Error -> Unit
            }

            when (val result = getAvailabilityUseCase(serviceId)) {
                is Resource.Success -> buildAvailableDays(result.data)
                is Resource.Error -> Unit
            }

            isLoading = false
        }
    }

    private fun buildAvailableDays(availability: BookingAvailability) {
        schedule = availability.schedule

        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayIso = isoFormat.format(Calendar.getInstance().time)

        // Solo fechas de hoy en adelante que tengan al menos un horario libre (no marcado ocupado)
        availableDays = schedule
            .filterKeys { it >= todayIso }
            .filterValues { slots -> slots.any { !it.occupied } }
            .keys
            .sorted()
            .mapNotNull { isoDate -> buildBookingDay(isoDate, isoFormat) }
    }

    private fun buildBookingDay(isoDate: String, isoFormat: SimpleDateFormat): BookingDay? {
        val date = runCatching { isoFormat.parse(isoDate) }.getOrNull() ?: return null
        val calendar = Calendar.getInstance().apply { time = date }
        val isoWeekDay = ((calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1
        return BookingDay(
            isoDate = isoDate,
            dayLabel = DAY_LABELS[isoWeekDay - 1],
            dayNumber = calendar.get(Calendar.DAY_OF_MONTH).toString(),
            monthLabel = "${MONTH_LABELS[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
        )
    }

    fun onDateSelected(day: BookingDay) {
        selectedDate = day
        selectedTime = null
        daySlots = schedule[day.isoDate].orEmpty()
        slots = daySlots.map { it.time }
        // Inicialmente los ocupados por el admin ya cuentan como tomados
        takenSlots = daySlots.filter { it.occupied }.map { it.time }

        viewModelScope.launch {
            when (val result = getTakenSlotsUseCase(serviceId, day.isoDate)) {
                is Resource.Success -> {
                    // Unión de ocupados por admin + reservados por otros usuarios
                    takenSlots = (takenSlots + result.data).distinct()
                }

                is Resource.Error -> Unit
            }
        }
    }

    fun onTimeSelected(time: String) {
        if (time in takenSlots) return
        selectedTime = time
    }

    fun clearError() {
        showError = false
    }

    fun confirmReservation(onOpenWhatsApp: (String) -> Unit) {
        val currentService = service ?: return
        val day = selectedDate ?: return
        val time = selectedTime ?: return
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true

            val reservation = ServiceReservation(
                serviceId = currentService.id,
                serviceName = currentService.title,
                durationLabel = Utilities.formatServiceDuration(currentService.duration),
                price = currentService.price,
                userId = SessionManager.currentUserId.value.orEmpty(),
                nameUser = SessionManager.nameUser.value.orEmpty(),
                emailUser = SessionManager.emailUser.value.orEmpty(),
                date = day.isoDate,
                dateLabel = "${day.dayLabel} ${day.dayNumber} " +
                        day.monthLabel.lowercase().replaceFirstChar { it.uppercase() },
                time = time
            )

            when (val result = createReservationUseCase(reservation)) {
                is Resource.Success -> {
                    reservationPlaced = result.data

                    val whatsapp = SessionManager.whatsApp.value
                        ?.takeIf { it.isNotEmpty() }
                        ?: Strings.defaultAdminWhatsapp
                    onOpenWhatsApp(
                        Utilities.createReservationMessageWhatsApp(result.data, whatsapp)
                    )
                }

                is Resource.Error -> showError = true
            }
            isLoading = false
        }
    }
}
