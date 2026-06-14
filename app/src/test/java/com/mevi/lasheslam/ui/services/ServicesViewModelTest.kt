package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateServiceUseCase
import com.mevi.lasheslam.domain.usecase.GetCategoriesServices
import com.mevi.lasheslam.domain.usecase.service.DeleteServiceUseCase
import com.mevi.lasheslam.domain.usecase.service.GetAServiceDetailUseCase
import com.mevi.lasheslam.domain.usecase.service.UpdateServiceUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ServicesViewModelTest {

    private val getCategoriesServices: GetCategoriesServices = mockk(relaxed = true)
    private val getAServiceDetailUseCase: GetAServiceDetailUseCase = mockk(relaxed = true)
    private val createServiceUseCase: CreateServiceUseCase = mockk()
    private val updateServiceUseCase: UpdateServiceUseCase = mockk()
    private val deleteServiceUseCase: DeleteServiceUseCase = mockk()
    private val getFacebookUseCase: GetFacebookUseCase = mockk(relaxed = true)
    private val getInstagramUseCase: GetInstagramUseCase = mockk(relaxed = true)
    private val getWhatsAppUseCase: GetWhatsAppUseCase = mockk(relaxed = true)
    private val analytics: AnalyticsTracker = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = ServicesViewModel(
        getCategoriesServices,
        getAServiceDetailUseCase,
        createServiceUseCase,
        updateServiceUseCase,
        deleteServiceUseCase,
        getFacebookUseCase,
        getInstagramUseCase,
        getWhatsAppUseCase,
        analytics
    )

    @Test
    fun `form change handlers update the form state`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onTitleChange("Pestañas")
        viewModel.onCostChange("250")

        assertEquals("Pestañas", viewModel.uiState.value.form.titulo)
        assertEquals("250", viewModel.uiState.value.form.precio)
    }

    @Test
    fun `saveService emits ServiceSaved on success`() = runTest {
        coEvery { createServiceUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = buildViewModel()
        val events = mutableListOf<ServiceUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveService()
        advanceUntilIdle()

        assertTrue(events.contains(ServiceUiEvent.ServiceSaved))
        job.cancel()
    }

    @Test
    fun `saveService emits ShowError on failure`() = runTest {
        coEvery { createServiceUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        val events = mutableListOf<ServiceUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveService()
        advanceUntilIdle()

        assertTrue(events.any { it is ServiceUiEvent.ShowError })
        job.cancel()
    }
}
