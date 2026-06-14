package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveSessionUseCaseTest {

    private val repository: SessionRepository = mockk(relaxed = true)
    private val useCase = SaveSessionUseCase(repository)

    @Test
    fun `marks user as admin when email is in admin list`() = runTest {
        every { repository.isAdmin("admin@test.com") } returns true

        useCase("admin@test.com")

        verify { repository.setAdmin(true) }
        verify { repository.setSessionManager() }
    }

    @Test
    fun `marks user as non admin when email is not in admin list`() = runTest {
        every { repository.isAdmin("user@test.com") } returns false

        useCase("user@test.com")

        verify { repository.setAdmin(false) }
    }

    @Test
    fun `resolves admin before persisting the session`() = runTest {
        every { repository.isAdmin(any()) } returns true

        useCase("admin@test.com")

        verifyOrder {
            repository.setAdmin(true)
            repository.setSessionManager()
        }
    }
}
