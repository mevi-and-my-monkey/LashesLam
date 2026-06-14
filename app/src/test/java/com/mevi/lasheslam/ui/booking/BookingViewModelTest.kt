package com.mevi.lasheslam.ui.booking

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.booking.CreateReservationUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetTakenSlotsUseCase
import com.mevi.lasheslam.domain.usecase.service.GetAServiceDetailUseCase
import com.mevi.lasheslam.network.BookingAvailability
import com.mevi.lasheslam.network.BookingSlot
import com.mevi.lasheslam.network.CreateServiceDto
import com.mevi.lasheslam.network.ServiceReservation
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BookingViewModelTest {

    private val getAServiceDetailUseCase: GetAServiceDetailUseCase = mockk()
    private val getAvailabilityUseCase: GetAvailabilityUseCase = mockk()
    private val getTakenSlotsUseCase: GetTakenSlotsUseCase = mockk(relaxed = true)
    private val createReservationUseCase: CreateReservationUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val service = CreateServiceDto(id = "s1", title = "Lash Lifting", price = 350.0, duration = 1.0)

    // Fecha lejana para que siempre pase el filtro "de hoy en adelante".
    private val futureDate = "2999-12-31"

    private fun buildViewModel() = BookingViewModel(
        getAServiceDetailUseCase,
        getAvailabilityUseCase,
        getTakenSlotsUseCase,
        createReservationUseCase
    )

    @Test
    fun `load fills service and available days from use cases`() = runTest {
        coEvery { getAServiceDetailUseCase("s1") } returns Resource.Success(service)
        coEvery { getAvailabilityUseCase("s1") } returns Resource.Success(
            BookingAvailability(schedule = mapOf(futureDate to listOf(BookingSlot("10:00", occupied = false))))
        )

        val viewModel = buildViewModel()
        viewModel.load("s1")
        advanceUntilIdle()

        assertEquals(service, viewModel.service)
        assertEquals(1, viewModel.availableDays.size)
        assertEquals(futureDate, viewModel.availableDays.first().isoDate)
    }

    @Test
    fun `onTimeSelected ignores a taken slot`() = runTest {
        coEvery { getAServiceDetailUseCase(any()) } returns Resource.Success(service)
        coEvery { getAvailabilityUseCase(any()) } returns Resource.Success(
            BookingAvailability(schedule = mapOf(futureDate to listOf(BookingSlot("10:00", occupied = true))))
        )
        coEvery { getTakenSlotsUseCase(any(), any()) } returns Resource.Success(emptyList())

        val viewModel = buildViewModel()
        viewModel.load("s1")
        advanceUntilIdle()
        viewModel.onDateSelected(BookingDay(futureDate, "Vie", "31", "DICIEMBRE 2999"))
        advanceUntilIdle()

        // "10:00" está marcado como ocupado por el admin, debe ignorarse
        viewModel.onTimeSelected("10:00")

        assertEquals(null, viewModel.selectedTime)
    }

    @Test
    fun `confirmReservation does nothing when nothing is selected`() = runTest {
        val viewModel = buildViewModel()

        var opened = false
        viewModel.confirmReservation { opened = true }
        advanceUntilIdle()

        assertTrue(!opened)
        coVerify(exactly = 0) { createReservationUseCase(any()) }
    }

    @Test
    fun `confirmReservation success stores reservation and opens whatsapp`() = runTest {
        coEvery { getAServiceDetailUseCase(any()) } returns Resource.Success(service)
        coEvery { getAvailabilityUseCase(any()) } returns Resource.Success(
            BookingAvailability(schedule = mapOf(futureDate to listOf(BookingSlot("10:00", occupied = false))))
        )
        coEvery { getTakenSlotsUseCase(any(), any()) } returns Resource.Success(emptyList())
        coEvery { createReservationUseCase(any()) } returns
            Resource.Success(ServiceReservation(serviceId = "s1", time = "10:00"))

        val viewModel = buildViewModel()
        viewModel.load("s1")
        advanceUntilIdle()
        viewModel.onDateSelected(viewModel.availableDays.first())
        advanceUntilIdle()
        viewModel.onTimeSelected("10:00")

        var url: String? = null
        viewModel.confirmReservation { url = it }
        advanceUntilIdle()

        assertNotNull(viewModel.reservationPlaced)
        assertTrue(url?.startsWith("https://wa.me/") == true)
    }
}
