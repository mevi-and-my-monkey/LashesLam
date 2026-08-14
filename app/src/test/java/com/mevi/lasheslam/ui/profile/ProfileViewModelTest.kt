package com.mevi.lasheslam.ui.profile

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import com.mevi.lasheslam.domain.usecase.GetUserProfileUseCase
import com.mevi.lasheslam.domain.usecase.SignOutUseCase
import com.mevi.lasheslam.domain.usecase.UpdateAddressUseCase
import com.mevi.lasheslam.domain.usecase.UpdatePhoneUseCase
import com.mevi.lasheslam.domain.usecase.UpdateUserPhotoUseCase
import com.mevi.lasheslam.domain.usecase.cart.ClearCartUseCase
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel

    // Mocks
    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk(relaxed = true)
    private val updateAddressUseCase: UpdateAddressUseCase = mockk(relaxed = true)
    private val updatePhoneUseCase: UpdatePhoneUseCase = mockk(relaxed = true)
    private val signOutUseCase: SignOutUseCase = mockk(relaxed = true)
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase = mockk(relaxed = true)
    private val clearCartUseCase: ClearCartUseCase = mockk(relaxed = true)
    private val sessionDataSource: SessionDataSource = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { userPreferencesRepository.darkMode } returns flowOf(false)

        viewModel = ProfileViewModel(
            userPreferencesRepository = userPreferencesRepository,
            getUserProfileUseCase = getUserProfileUseCase,
            updateAddressUseCase = updateAddressUseCase,
            updatePhoneUseCase = updatePhoneUseCase,
            signOutUseCase = signOutUseCase,
            updateUserPhotoUseCase = updateUserPhotoUseCase,
            clearCartUseCase = clearCartUseCase,
            sessionDataSource = sessionDataSource
        )
    }

    // ---------- TOGGLE DARK MODE ----------
    @Test
    fun `toggleDarkMode calls repository with correct value`() = runTest {
        coEvery { userPreferencesRepository.setDarkMode(true) } just Runs

        viewModel.toggleDarkMode(true)
        advanceUntilIdle()

        coVerify { userPreferencesRepository.setDarkMode(true) }
    }

    // ---------- UPDATE ADDRESS ----------
    @Test
    fun `updateAddress with blank address should fail`() {
        var success = true
        var message: String? = null

        viewModel.updateAddress("") { s, m ->
            success = s
            message = m
        }

        assertFalse(success)
        assertEquals("La dirección no puede estar vacía", message)
    }

    @Test
    fun `updateAddress success updates state and calls onResult true`() = runTest {
        coEvery { updateAddressUseCase("Calle 1 #23") } returns Resource.Success(Unit)

        var success = false
        var message: String? = null

        viewModel.updateAddress("Calle 1 #23") { s, m ->
            success = s
            message = m
        }
        advanceUntilIdle()

        assertTrue(success)
        assertEquals(null, message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `updateAddress failure calls onResult false`() = runTest {
        coEvery { updateAddressUseCase("Calle 2 #45") } returns Resource.Error(AppError.Network)

        var success = true
        var message: String? = null

        viewModel.updateAddress("Calle 2 #45") { s, m ->
            success = s
            message = m
        }
        advanceUntilIdle()

        assertFalse(success)
        assertEquals("Error al actualizar dirección", message)
    }

    // ---------- UPDATE PHONE ----------
    @Test
    fun `updatePhone success updates phone and calls onResult true`() = runTest {
        coEvery { updatePhoneUseCase("5551234567") } returns Resource.Success(Unit)

        var success = false
        var message: String? = null

        viewModel.updatePhone("5551234567") { s, m ->
            success = s
            message = m
        }
        advanceUntilIdle()

        assertTrue(success)
        assertEquals(null, message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `updatePhone failure calls onResult false`() = runTest {
        coEvery { updatePhoneUseCase("5559876543") } returns Resource.Error(AppError.Network)

        var success = true
        var message: String? = null

        viewModel.updatePhone("5559876543") { s, m ->
            success = s
            message = m
        }
        advanceUntilIdle()

        assertFalse(success)
        assertEquals("Error al actualizar el numero telefonico", message)
    }

    // ---------- SIGN OUT ----------
    @Test
    fun `signOut clears cart, wipes session, signs out and runs navigation callback`() {
        var navigated = false

        viewModel.signOut { navigated = true }

        verify { clearCartUseCase() }
        verify { sessionDataSource.clearUserSession() }
        verify { signOutUseCase() }
        assertTrue(navigated)
    }
}
