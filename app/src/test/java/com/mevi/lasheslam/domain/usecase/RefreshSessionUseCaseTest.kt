package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshSessionUseCaseTest {

    private val repository: SessionRepository = mockk(relaxed = true)
    private val useCase = RefreshSessionUseCase(repository)

    @Test
    fun `refreshes session and loads name, photo and admin state`() = runTest {
        every { repository.getEmail() } returns "user@test.com"
        every { repository.isAdmin("user@test.com") } returns true

        useCase()

        coVerify(exactly = 1) { repository.refreshSession() }
        coVerify(exactly = 1) { repository.setName() }
        coVerify(exactly = 1) { repository.setPhoto() }
        verify { repository.setAdmin(true) }
        verify { repository.setSessionManager() }
    }

    // Reproduce el bug: si setName() falla, la excepción se traga (runCatching)
    // y el resto del flujo continúa, dejando el nombre sin cargar y sin avisar.
    @Test
    fun `swallows setName failure and still loads photo and session`() = runTest {
        every { repository.getEmail() } returns "user@test.com"
        every { repository.isAdmin(any()) } returns false
        coEvery { repository.setName() } throws RuntimeException("Firestore down")

        // No debe propagar la excepción
        useCase()

        coVerify { repository.setPhoto() }
        verify { repository.setSessionManager() }
    }

    @Test
    fun `does not touch admin state when there is no email`() = runTest {
        every { repository.getEmail() } returns null

        useCase()

        coVerify { repository.setName() }
        coVerify { repository.setPhoto() }
        verify(exactly = 0) { repository.setAdmin(any()) }
        verify(exactly = 0) { repository.setSessionManager() }
    }
}
