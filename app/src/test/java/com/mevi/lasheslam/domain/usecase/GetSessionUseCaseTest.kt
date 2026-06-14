package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class GetSessionUseCaseTest {

    private val repository: SessionRepository = mockk()
    private val useCase = GetSessionUseCase(repository)

    @Test
    fun `returns true when user is logged in`() {
        every { repository.isLoggedIn() } returns true
        assertTrue(useCase())
    }

    @Test
    fun `returns false when user is not logged in`() {
        every { repository.isLoggedIn() } returns false
        assertFalse(useCase())
    }
}
