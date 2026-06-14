package com.mevi.lasheslam.ui.profile.request

import com.mevi.lasheslam.domain.usecase.GetServicesUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetReservationsByStatusUseCase
import com.mevi.lasheslam.domain.usecase.booking.SaveAvailabilityUseCase
import com.mevi.lasheslam.domain.usecase.booking.UpdateReservationStatusUseCase
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AdminReservationsViewModelTest {

    private val getReservationsByStatusUseCase: GetReservationsByStatusUseCase = mockk(relaxed = true)
    private val updateReservationStatusUseCase: UpdateReservationStatusUseCase = mockk(relaxed = true)
    private val getAvailabilityUseCase: GetAvailabilityUseCase = mockk(relaxed = true)
    private val saveAvailabilityUseCase: SaveAvailabilityUseCase = mockk(relaxed = true)
    private val getServicesUseCase: GetServicesUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = AdminReservationsViewModel(
        getReservationsByStatusUseCase,
        updateReservationStatusUseCase,
        getAvailabilityUseCase,
        saveAvailabilityUseCase,
        getServicesUseCase
    )

    @Test
    fun `addSlot inserts a valid time and keeps slots sorted`() {
        val viewModel = buildViewModel()
        viewModel.selectEditorDate("2026-06-20")

        viewModel.addSlot("14:00")
        viewModel.addSlot("09:30")

        val times = viewModel.slotsForSelectedDate().map { it.time }
        assertEquals(listOf("09:30", "14:00"), times)
    }

    @Test
    fun `addSlot ignores malformed time and duplicates`() {
        val viewModel = buildViewModel()
        viewModel.selectEditorDate("2026-06-20")

        viewModel.addSlot("9am")     // formato inválido
        viewModel.addSlot("10:00")
        viewModel.addSlot("10:00")   // duplicado

        assertEquals(listOf("10:00"), viewModel.slotsForSelectedDate().map { it.time })
    }

    @Test
    fun `removeSlot deletes the matching time`() {
        val viewModel = buildViewModel()
        viewModel.selectEditorDate("2026-06-20")
        viewModel.addSlot("10:00")
        viewModel.addSlot("11:00")

        viewModel.removeSlot("10:00")

        assertEquals(listOf("11:00"), viewModel.slotsForSelectedDate().map { it.time })
    }

    @Test
    fun `toggleSlotOccupied flips the occupied flag`() {
        val viewModel = buildViewModel()
        viewModel.selectEditorDate("2026-06-20")
        viewModel.addSlot("10:00")

        viewModel.toggleSlotOccupied("10:00")

        assertTrue(viewModel.slotsForSelectedDate().first().occupied)
    }

    @Test
    fun `saveAvailability returns false when no service is selected`() = runTest {
        val viewModel = buildViewModel()

        var result: Boolean? = null
        viewModel.saveAvailability { result = it }
        advanceUntilIdle()

        assertFalse(result == true)
    }
}
