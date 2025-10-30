package com.mevi.lasheslam.ui.profile

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.navigation.Screen
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
    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private val dataStoreRepository: DataStoreRepository = mockk(relaxed = true)
    private val navController: NavController = mockk(relaxed = true)

    private val user: FirebaseUser = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        // DataStore dark mode
        every { dataStoreRepository.darkMode } returns flowOf(false)

        // Auth user
        every { auth.currentUser } returns user

        viewModel = ProfileViewModel(firestore, auth, dataStoreRepository)
    }

    // ---------- TOGGLE DARK MODE ----------
    @Test
    fun `toggleDarkMode calls repository with correct value`() = runTest {
        coEvery { dataStoreRepository.setDarkMode(true) } just Runs

        viewModel.toggleDarkMode(true)
        advanceUntilIdle()

        coVerify { dataStoreRepository.setDarkMode(true) }
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
    fun `updateAddress success updates state and calls onResult true`() {
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val voidTask = mockk<Task<Void>>(relaxed = true)

        every { user.uid } returns "123"
        every { firestore.collection("users").document("123") } returns documentRef
        every { documentRef.update("address", any()) } returns voidTask
        every { voidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(mockk())
            voidTask
        }
        every { voidTask.addOnFailureListener(any<OnFailureListener>()) } answers { voidTask }

        var success = false
        var message: String? = null

        viewModel.updateAddress("Calle 1 #23") { s, m ->
            success = s
            message = m
        }

        assertTrue(success)
        assertEquals(null, message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `updateAddress failure calls onResult false`() {
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val voidTask = mockk<Task<Void>>(relaxed = true)

        every { user.uid } returns "123"
        every { firestore.collection("users").document("123") } returns documentRef
        every { documentRef.update("address", any()) } returns voidTask
        every { voidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } returns voidTask
        every { voidTask.addOnFailureListener(any<OnFailureListener>()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception("Firestore error"))
            voidTask
        }

        var success = true
        var message: String? = null

        viewModel.updateAddress("Calle 2 #45") { s, m ->
            success = s
            message = m
        }

        assertFalse(success)
        assertEquals("Error al actualizar dirección", message)
    }

    // ---------- UPDATE PHONE ----------
    @Test
    fun `updatePhone success updates phone and calls onResult true`() {
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val voidTask = mockk<Task<Void>>(relaxed = true)

        every { user.uid } returns "123"
        every { firestore.collection("users").document("123") } returns documentRef
        every { documentRef.update("phone", any()) } returns voidTask
        every { voidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } answers {
            val listener = firstArg<OnSuccessListener<Void>>()
            listener.onSuccess(mockk())
            voidTask
        }
        every { voidTask.addOnFailureListener(any<OnFailureListener>()) } answers { voidTask }

        var success = false
        var message: String? = null

        viewModel.updatePhone("5551234567") { s, m ->
            success = s
            message = m
        }

        assertTrue(success)
        assertEquals(null, message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `updatePhone failure calls onResult false`() {
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val voidTask = mockk<Task<Void>>(relaxed = true)

        every { user.uid } returns "123"
        every { firestore.collection("users").document("123") } returns documentRef
        every { documentRef.update("phone", any()) } returns voidTask
        every { voidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } returns voidTask
        every { voidTask.addOnFailureListener(any<OnFailureListener>()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(Exception("Error"))
            voidTask
        }

        var success = true
        var message: String? = null

        viewModel.updatePhone("5559876543") { s, m ->
            success = s
            message = m
        }

        assertFalse(success)
        assertEquals("Error al actualizar el numero telefonico", message)
    }

    // ---------- SIGN OUT ----------
    @Test
    fun `signOut calls FirebaseAuth signOut and navigates to Login`() {
        every { navController.navigate(any<String>(), any<NavOptionsBuilder.() -> Unit>()) } just Runs

        viewModel.signOut(navController)

        verify { auth.signOut() }
        verify {
            navController.navigate(Screen.Login.route, any<NavOptionsBuilder.() -> Unit>())
        }
    }
}