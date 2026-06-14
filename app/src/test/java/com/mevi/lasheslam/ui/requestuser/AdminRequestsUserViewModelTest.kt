package com.mevi.lasheslam.ui.requestuser

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetAllRequestsUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.booking.GetUserReservationsUseCase
import com.mevi.lasheslam.domain.usecase.cart.GetUserProductOrdersUseCase
import com.mevi.lasheslam.network.CourseRequest
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AdminRequestsUserViewModelTest {

    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    private val getIsAdminUseCase: GetIsAdminUseCase = mockk(relaxed = true)
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase = mockk(relaxed = true)
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = mockk(relaxed = true)
    private val getNameUserUseCase: GetNameUserUseCase = mockk(relaxed = true)
    private val getPhotoUserUseCase: GetPhotoUserUseCase = mockk(relaxed = true)
    private val getAllRequestsUseCase: GetAllRequestsUseCase = mockk()
    private val getUserProductOrdersUseCase: GetUserProductOrdersUseCase = mockk(relaxed = true)
    private val getUserReservationsUseCase: GetUserReservationsUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = AdminRequestsUserViewModel(
        analytics,
        getIsAdminUseCase,
        getIsUserInvitedUseCase,
        getCurrentUserIdUseCase,
        getNameUserUseCase,
        getPhotoUserUseCase,
        getAllRequestsUseCase,
        getUserProductOrdersUseCase,
        getUserReservationsUseCase
    )

    @Test
    fun `onSectionSelected updates section and tracks analytics`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSectionSelected(Section.PRODUCTOS)

        assertEquals(Section.PRODUCTOS, viewModel.uiState.value.selectedSection)
        verify { analytics.track(AnalyticsEvent.SectionSelected(Section.PRODUCTOS.name)) }
    }

    @Test
    fun `loadRequests stores course requests on success`() = runTest {
        val data = listOf(CourseRequest(nameUser = "Ana"))
        coEvery { getAllRequestsUseCase("u1") } returns Resource.Success(data)

        val viewModel = buildViewModel()
        viewModel.loadRequests("u1")
        advanceUntilIdle()

        assertEquals(data, viewModel.uiState.value.requestUserCourses)
    }

    @Test
    fun `loadRequests emits ShowError on failure`() = runTest {
        coEvery { getAllRequestsUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        val events = mutableListOf<RequestUserUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.loadRequests("u1")
        advanceUntilIdle()

        assertTrue(events.any { it is RequestUserUiEvent.ShowError })
        job.cancel()
    }
}
