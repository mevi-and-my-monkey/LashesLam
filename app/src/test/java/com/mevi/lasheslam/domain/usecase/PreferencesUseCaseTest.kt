package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class PreferencesUseCaseTest {

    @Test
    fun `GetDarkModeUseCase exposes the repository flow`() {
        val repo: UserPreferencesRepository = mockk()
        val flow: Flow<Boolean> = flowOf(true)
        every { repo.darkMode } returns flow

        assertSame(flow, GetDarkModeUseCase(repo)())
    }

    @Test
    fun `SaveIsDarkModeUseCase forwards the value to the repository`() = runTest {
        val repo: DataStoreRepository = mockk(relaxed = true)

        SaveIsDarkModeUseCase(repo)(true)

        coVerify { repo.setDarkMode(true) }
    }
}
