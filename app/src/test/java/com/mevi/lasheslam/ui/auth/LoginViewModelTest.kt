package com.mevi.lasheslam.ui.auth

import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.AuthState
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = LoginViewModel(loginUseCase, registerUseCase, googleUseCase)
    }

    @Test
    fun `email and password validation works`() {
        viewModel.onLoginChanged("test@example.com", "1234567")
        assertTrue(viewModel.isLoginEnable)

        viewModel.onLoginChanged("invalid", "1234567")
        assertFalse(viewModel.isLoginEnable)
    }

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
        assertFalse(viewModel.isLoading)
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
        assertFalse(viewModel.isLoading)
        assertFalse(called)
        assertEquals("Invalid credentials", message)
    }

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
}