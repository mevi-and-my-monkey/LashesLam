package com.mevi.lasheslam.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveSessionUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mockk()
    private val registerUseCase: RegisterUseCase = mockk()
    private val googleUseCase: SignInWithGoogleUseCase = mockk()
    private val saveSessionUseCase: SaveSessionUseCase = mockk()
    private val errorMapper : ErrorMapper = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            registerUseCase = registerUseCase,
            googleUseCase = googleUseCase,
            saveSessionUseCase = saveSessionUseCase,
            errorMapper = errorMapper
        )
    }

    @Test
    fun `login success emits NavigateToHome`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Success(true)
        coEvery { saveSessionUseCase(any()) } returns Unit

        viewModel.onLoginChanged("test@example.com", "password123")

        val events = mutableListOf<LoginUiEvent>()

        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        viewModel.login()
        advanceUntilIdle()

        assertTrue(events.contains(LoginUiEvent.NavigateToHome))

        job.cancel()
    }

    @Test
    fun `login error emits ShowError`() = runTest {
        val appError = AppError.InvalidCredentials

        coEvery { loginUseCase(any(), any()) } returns Resource.Error(appError)
        coEvery { errorMapper.map(any()) } returns appError

        viewModel.onLoginChanged("test@example.com", "wrongpass")

        val events = mutableListOf<LoginUiEvent>()

        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        viewModel.login()
        advanceUntilIdle()

        val errorEvent = events.filterIsInstance<LoginUiEvent.ShowError>().firstOrNull()

        assertEquals(appError.toUserMessage(), errorEvent?.error?.toUserMessage() ?: "")

        job.cancel()
    }

    @Test
    fun `google sign in success emits NavigateToHome`() = runTest {
        val credential = mockk<AuthCredential>()
        coEvery { googleUseCase(credential) } returns Resource.Success(true)
        coEvery { saveSessionUseCase(any()) } returns Unit

        val events = mutableListOf<LoginUiEvent>()

        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        viewModel.signInWithGoogle(credential, "test@example.com")
        advanceUntilIdle()

        assertTrue(events.contains(LoginUiEvent.NavigateToHome))

        job.cancel()
    }

    @Test
    fun `register success emits RegisterSuccess`() = runTest {
        val user = UserModel("123", "test@example.com", "Test User")

        coEvery { registerUseCase(user) } returns Resource.Success(true)
        coEvery { saveSessionUseCase(any()) } returns Unit

        val events = mutableListOf<LoginUiEvent>()

        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        viewModel.register(user)
        advanceUntilIdle()

        assertTrue(events.contains(LoginUiEvent.RegisterSuccess))

        job.cancel()
    }

    @Test
    fun `onError emits ShowError event`() = runTest {
        val exception = Exception("test")
        val appError = AppError.Unknown("test")

        coEvery { errorMapper.map(exception) } returns appError

        val events = mutableListOf<LoginUiEvent>()

        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        viewModel.onError(exception)
        advanceUntilIdle()

        val errorEvent = events.filterIsInstance<LoginUiEvent.ShowError>().firstOrNull()

        assertEquals(appError.toUserMessage(), errorEvent?.error?.toUserMessage() ?: "")

        job.cancel()
    }

    @Test
    fun `email and password validation works`() {
        viewModel.onLoginChanged("test@example.com", "1234567")
        assertTrue(viewModel.uiState.value.isLoginEnabled)

        viewModel.onLoginChanged("invalid", "1234567")
        assertFalse(viewModel.uiState.value.isLoginEnabled)
    }


}