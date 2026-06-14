package com.mevi.lasheslam.ui.splashscreen

import com.mevi.lasheslam.core.results.UpdateResult
import com.mevi.lasheslam.domain.usecase.CheckUpdateUseCase
import com.mevi.lasheslam.domain.usecase.GetSessionUseCase
import com.mevi.lasheslam.domain.usecase.RefreshSessionUseCase
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    private val checkUpdateUseCase: CheckUpdateUseCase = mockk()
    private val getSessionUseCase: GetSessionUseCase = mockk()
    private val refreshSessionUseCase: RefreshSessionUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() =
        SplashViewModel(checkUpdateUseCase, getSessionUseCase, refreshSessionUseCase)

    @Test
    fun `emits ForceUpdate and skips session when update is required`() = runTest {
        coEvery { checkUpdateUseCase() } returns UpdateResult.Required

        val viewModel = buildViewModel()
        viewModel.init()
        advanceUntilIdle()

        assertEquals(SplashEffect.ForceUpdate, viewModel.effect.replayCache.firstOrNull())
        coVerify(exactly = 0) { refreshSessionUseCase() }
    }

    @Test
    fun `emits NavigateHome when user is logged in`() = runTest {
        coEvery { checkUpdateUseCase() } returns UpdateResult.NotRequired
        every { getSessionUseCase() } returns true

        val viewModel = buildViewModel()
        viewModel.init()
        advanceUntilIdle()

        assertEquals(SplashEffect.NavigateHome, viewModel.effect.replayCache.firstOrNull())
        coVerify { refreshSessionUseCase() }
    }

    @Test
    fun `emits NavigateLogin when user is not logged in`() = runTest {
        coEvery { checkUpdateUseCase() } returns UpdateResult.NotRequired
        every { getSessionUseCase() } returns false

        val viewModel = buildViewModel()
        viewModel.init()
        advanceUntilIdle()

        assertEquals(SplashEffect.NavigateLogin, viewModel.effect.replayCache.firstOrNull())
    }

    @Test
    fun `init runs only once even if called multiple times`() = runTest {
        coEvery { checkUpdateUseCase() } returns UpdateResult.NotRequired
        every { getSessionUseCase() } returns true

        val viewModel = buildViewModel()
        viewModel.init()
        viewModel.init()
        advanceUntilIdle()

        coVerify(exactly = 1) { checkUpdateUseCase() }
    }
}
