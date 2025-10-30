package com.mevi.lasheslam.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.AuthState
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private val saveIsDarkModeUseCase: SaveIsDarkModeUseCase = mockk(relaxed = true)
    private val getIsDarkModeUseCase: GetIsDarkModeUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // simulamos el flujo de dark mode con un valor inicial
        every { getIsDarkModeUseCase.invoke() } returns flowOf(false)

        viewModel = LoginViewModel(
            loginUseCase,
            registerUseCase,
            googleUseCase,
            saveIsDarkModeUseCase,
            getIsDarkModeUseCase
        )
    }

    // ----------- VALIDACIONES DE LOGIN -----------

    @Test
    fun `email and password validation works`() {
        viewModel.onLoginChanged("test@example.com", "1234567")
        assertTrue(viewModel.isLoginEnable)

        viewModel.onLoginChanged("invalid", "1234567")
        assertFalse(viewModel.isLoginEnable)
    }

    // ----------- LOGIN -----------

    @Test
    fun `login success updates authState and stops loading`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Success(true)

        var called = false
        viewModel.onLoginChanged("test@example.com", "password123")

        viewModel.login { success, _ ->
            called = success
        }

        advanceUntilIdle()

        assertEquals(AuthState.Success, viewModel.authState)
        assertFalse(viewModel.isLoading.value!!)
        assertTrue(called)
    }

    @Test
    fun `login error updates authState with message`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Error("Invalid credentials")

        var called = false
        var message: String? = null

        viewModel.onLoginChanged("test@example.com", "wrongpass")

        viewModel.login { success, msg ->
            called = success
            message = msg
        }

        advanceUntilIdle()

        assertEquals(AuthState.Error("Invalid credentials"), viewModel.authState)
        assertFalse(viewModel.isLoading.value!!)
        assertFalse(called)
        assertEquals("Invalid credentials", message)
    }

    // ----------- REGISTER -----------

    @Test
    fun `register success sets authState success`() = runTest {
        val user = UserModel("123", "test@example.com", "Test User")
        coEvery { registerUseCase(user) } returns Resource.Success(true)

        var called = false
        viewModel.register(user) { success, _ ->
            called = success
        }

        advanceUntilIdle()

        assertEquals(AuthState.Success, viewModel.authState)
        assertTrue(called)
    }

    // ----------- GOOGLE SIGN-IN -----------

    @Test
    fun `sign in with Google success updates authState`() = runTest {
        val credential = mockk<AuthCredential>()
        coEvery { googleUseCase(credential) } returns Resource.Success(true)

        var called = false
        viewModel.signInWithGoogle(credential) { success, _ ->
            called = success
        }

        advanceUntilIdle()

        assertEquals(AuthState.Success, viewModel.authState)
        assertTrue(called)
    }

    // ----------- DARK MODE -----------

    @Test
    fun `toggleDarkMode calls saveIsDarkModeUseCase`() = runTest {
        coEvery { saveIsDarkModeUseCase(any()) } returns Unit

        viewModel.toggleDarkMode(true)

        advanceUntilIdle()

        coVerify { saveIsDarkModeUseCase(true) }
    }

    @Test
    fun `getIsDarkModeUseCase emits false initially`() = runTest {
        val value = viewModel.isDarkMode.value
        assertEquals(false, value)
    }
}