package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesUseCaseTest {

    private val repository: FavoritesRepository = mockk(relaxed = true)

    // ToggleFavorite es el único con lógica: decide agregar o quitar según el estado actual.
    @Test
    fun `toggle removes from favorites when item is already a favorite`() = runTest {
        coEvery { repository.removeFromFavorites("u1", "i1") } returns Resource.Success(true)

        val result = ToggleFavoriteUseCase(repository)("u1", "i1", "course", isFavorite = true)

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.removeFromFavorites("u1", "i1") }
        coVerify(exactly = 0) { repository.addToFavorites(any(), any(), any()) }
    }

    @Test
    fun `toggle adds to favorites when item is not a favorite`() = runTest {
        coEvery { repository.addToFavorites("u1", "i1", "course") } returns Resource.Success(true)

        val result = ToggleFavoriteUseCase(repository)("u1", "i1", "course", isFavorite = false)

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.addToFavorites("u1", "i1", "course") }
        coVerify(exactly = 0) { repository.removeFromFavorites(any(), any()) }
    }

    @Test
    fun `AddFavoriteUseCase delegates to repository`() = runTest {
        AddFavoriteUseCase(repository)("u1", "i1", "product")
        coVerify { repository.addToFavorites("u1", "i1", "product") }
    }

    @Test
    fun `RemoveFavoriteUseCase delegates to repository`() = runTest {
        RemoveFavoriteUseCase(repository)("u1", "i1")
        coVerify { repository.removeFromFavorites("u1", "i1") }
    }

    @Test
    fun `GetFavoritesUseCase delegates to repository`() = runTest {
        GetFavoritesUseCase(repository)("u1")
        coVerify { repository.getFavorites("u1") }
    }
}
