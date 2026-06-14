package com.mevi.lasheslam.ui.favorites

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoriteCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.products.GetFavoriteProductsUseCase
import com.mevi.lasheslam.domain.usecase.service.GetFavoriteServicesUseCase
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.FavoriteItem
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesViewModelTest {

    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    private val getIsAdminUseCase: GetIsAdminUseCase = mockk(relaxed = true)
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase = mockk(relaxed = true)
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = mockk(relaxed = true)
    private val getNameUserUseCase: GetNameUserUseCase = mockk(relaxed = true)
    private val getPhotoUserUseCase: GetPhotoUserUseCase = mockk(relaxed = true)
    private val getFavoritesUseCase: GetFavoritesUseCase = mockk()
    private val getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase = mockk()
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase = mockk(relaxed = true)
    private val getFavoriteServicesUseCase: GetFavoriteServicesUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = FavoritesViewModel(
        analytics,
        getIsAdminUseCase,
        getIsUserInvitedUseCase,
        getCurrentUserIdUseCase,
        getNameUserUseCase,
        getPhotoUserUseCase,
        getFavoritesUseCase,
        getFavoriteCoursesUseCase,
        getFavoriteProductsUseCase,
        getFavoriteServicesUseCase
    )

    @Test
    fun `loadFavoriteCourses maps favorite ids to course details`() = runTest {
        val course = mockk<CoursesItem>()
        coEvery { getFavoritesUseCase(any()) } returns
            Resource.Success(listOf(FavoriteItem(itemId = "c1", type = "COURSE")))
        coEvery { getFavoriteCoursesUseCase(listOf("c1")) } returns Resource.Success(listOf(course))

        val viewModel = buildViewModel()
        viewModel.loadFavoriteCourses()
        advanceUntilIdle()

        assertEquals(listOf(course), viewModel.uiState.value.favoriteCourses)
    }

    @Test
    fun `loadFavoriteCourses with no favorites leaves the list empty`() = runTest {
        coEvery { getFavoritesUseCase(any()) } returns Resource.Success(emptyList())

        val viewModel = buildViewModel()
        viewModel.loadFavoriteCourses()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favoriteCourses.isEmpty())
        coVerify(exactly = 0) { getFavoriteCoursesUseCase(any()) }
    }

    @Test
    fun `loadFavoriteCourses emits ShowError when favorites fail`() = runTest {
        coEvery { getFavoritesUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        val events = mutableListOf<FavoriteUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.loadFavoriteCourses()
        advanceUntilIdle()

        assertTrue(events.any { it is FavoriteUiEvent.ShowError })
        job.cancel()
    }
}
