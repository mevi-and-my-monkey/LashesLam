package com.mevi.lasheslam.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val getPhotoUserUseCase: GetPhotoUserUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun `exposes the photo url emitted by the use case`() = runTest {
        every { getPhotoUserUseCase() } returns flowOf("http://photo")

        val viewModel = HomeViewModel(getPhotoUserUseCase)
        advanceUntilIdle()

        assertEquals("http://photo", viewModel.photoUrl)
    }

    @Test
    fun `loading helpers toggle the loading state`() = runTest {
        every { getPhotoUserUseCase() } returns flowOf(null)

        val viewModel = HomeViewModel(getPhotoUserUseCase)
        advanceUntilIdle()

        viewModel.showLoading()
        assertEquals(true, viewModel.isLoading.value)

        viewModel.hideLoading()
        assertEquals(false, viewModel.isLoading.value)
    }
}
