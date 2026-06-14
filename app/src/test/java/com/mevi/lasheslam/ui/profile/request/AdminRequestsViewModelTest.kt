package com.mevi.lasheslam.ui.profile.request

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.ApproveRequestUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.RejectRequestUseCase
import com.mevi.lasheslam.network.CourseRequest
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AdminRequestsViewModelTest {

    private val getRequestsUseCase: GetRequestsUseCase = mockk()
    private val approveRequestUseCase: ApproveRequestUseCase = mockk(relaxed = true)
    private val rejectRequestUseCase: RejectRequestUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private fun buildViewModel() =
        AdminRequestsViewModel(getRequestsUseCase, approveRequestUseCase, rejectRequestUseCase)

    @Test
    fun `loadRequests stores the returned requests`() = runTest {
        val data = listOf(CourseRequest(nameUser = "Ana"))
        coEvery { getRequestsUseCase("pendiente") } returns Resource.Success(data)

        val viewModel = buildViewModel()
        viewModel.loadRequests("pendiente")
        advanceUntilIdle()

        assertEquals(data, viewModel.requests)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `loadRequests keeps list empty on error`() = runTest {
        coEvery { getRequestsUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        viewModel.loadRequests("pendiente")
        advanceUntilIdle()

        assertTrue(viewModel.requests.isEmpty())
    }

    @Test
    fun `approve calls use case and notifies completion`() = runTest {
        val viewModel = buildViewModel()
        var done = false

        viewModel.approve("req1") { done = true }
        advanceUntilIdle()

        coVerify { approveRequestUseCase("req1") }
        assertTrue(done)
    }

    @Test
    fun `reject calls use case and notifies completion`() = runTest {
        val viewModel = buildViewModel()
        var done = false

        viewModel.reject("req1") { done = true }
        advanceUntilIdle()

        coVerify { rejectRequestUseCase("req1") }
        assertTrue(done)
    }
}
