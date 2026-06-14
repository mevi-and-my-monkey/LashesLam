package com.mevi.lasheslam.ui.auth

import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RefreshSessionUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveSessionUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.domain.usecase.ValidateLoginUseCase
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
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
    private val saveSessionUseCase: SaveSessionUseCase = mockk(relaxed = true)
    private val refreshSessionUseCase: RefreshSessionUseCase = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    // Se usa la implementación real porque es lógica pura sin dependencias.
    private val validateLoginUseCase = ValidateLoginUseCase()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            registerUseCase = registerUseCase,
            googleUseCase = googleUseCase,
            saveSessionUseCase = saveSessionUseCase,
            refreshSessionUseCase = refreshSessionUseCase,
            errorMapper = errorMapper,
            validateLoginUseCase = validateLoginUseCase,
            analytics = analytics
        )
    }

    @Test
    fun `login success emits NavigateToHome`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Success(true)

        viewModel.onLoginChanged("test@example.com", "password123")

        val events = mutableListOf<LoginUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.login()
        advanceUntilIdle()

        assertTrue(events.contains(LoginUiEvent.NavigateToHome))
        job.cancel()
    }

    // Protege el orden que arregla el bug del nombre: la sesión debe guardarse
    // y refrescarse (carga nombre/foto) ANTES de navegar a Home.
    @Test
    fun `login success saves and refreshes session before navigating`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Success(true)

        viewModel.onLoginChanged("test@example.com", "password123")
        viewModel.login()
        advanceUntilIdle()

        coVerifyOrder {
            saveSessionUseCase("test@example.com")
            refreshSessionUseCase()
        }
    }

    @Test
    fun `login error emits ShowError`() = runTest {
        val appError = AppError.InvalidCredentials
        coEvery { loginUseCase(any(), any()) } returns Resource.Error(appError)

        viewModel.onLoginChanged("test@example.com", "wrongpass")

        val events = mutableListOf<LoginUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.login()
        advanceUntilIdle()

        val errorEvent = events.filterIsInstance<LoginUiEvent.ShowError>().firstOrNull()
        assertEquals(appError, errorEvent?.error)
        coVerify(exactly = 0) { saveSessionUseCase(any()) }
        job.cancel()
    }

    @Test
    fun `google sign in success emits NavigateToHome`() = runTest {
        val credential = mockk<AuthCredential>()
        coEvery { googleUseCase(credential) } returns Resource.Success(true)

        val events = mutableListOf<LoginUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.signInWithGoogle(credential, "test@example.com")
        advanceUntilIdle()

        assertTrue(events.contains(LoginUiEvent.NavigateToHome))
        coVerify { refreshSessionUseCase() }
        job.cancel()
    }

    @Test
    fun `register success emits RegisterSuccess`() = runTest {
        val user = UserModel(uid = "123", email = "test@example.com", name = "Test User")
        coEvery { registerUseCase(user) } returns Resource.Success(true)

        val events = mutableListOf<LoginUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

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
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.onError(exception)
        advanceUntilIdle()

        val errorEvent = events.filterIsInstance<LoginUiEvent.ShowError>().firstOrNull()
        assertEquals(appError, errorEvent?.error)
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
