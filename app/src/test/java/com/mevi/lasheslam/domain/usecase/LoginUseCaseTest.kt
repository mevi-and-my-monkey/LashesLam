package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    private val repository: UserRepository = mockk()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `returns error and does not hit repository when email is blank`() = runTest {
        val result = useCase("", "password")

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).error is AppError.Unknown)
        coVerify(exactly = 0) { repository.signIn(any(), any()) }
    }

    @Test
    fun `returns error and does not hit repository when password is blank`() = runTest {
        val result = useCase("a@b.com", "")

        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { repository.signIn(any(), any()) }
    }

    @Test
    fun `delegates to repository when credentials are present`() = runTest {
        coEvery { repository.signIn("a@b.com", "password") } returns Resource.Success(true)

        val result = useCase("a@b.com", "password")

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.signIn("a@b.com", "password") }
    }
}
