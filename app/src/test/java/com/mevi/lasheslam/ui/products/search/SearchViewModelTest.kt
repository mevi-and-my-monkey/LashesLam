package com.mevi.lasheslam.ui.products.search

import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import com.mevi.lasheslam.domain.usecase.GetCategoriesServices
import com.mevi.lasheslam.domain.usecase.GetCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetProductsUseCase
import com.mevi.lasheslam.domain.usecase.GetServicesUseCase
import com.mevi.lasheslam.domain.usecase.ObserveFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    private val getACoursesUseCase: GetCoursesUseCase = mockk(relaxed = true)
    private val getCategoriesProducts: GetCategoriesProducts = mockk(relaxed = true)
    private val getProductsUseCase: GetProductsUseCase = mockk(relaxed = true)
    private val getCategoriesServices: GetCategoriesServices = mockk(relaxed = true)
    private val getServicesUseCase: GetServicesUseCase = mockk(relaxed = true)
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)
    private val observeFavoritesUseCase: ObserveFavoritesUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = SearchViewModel(
        analytics,
        getACoursesUseCase,
        getCategoriesProducts,
        getProductsUseCase,
        getCategoriesServices,
        getServicesUseCase,
        toggleFavoriteUseCase,
        observeFavoritesUseCase
    )

    @Test
    fun `onSearchChanged updates the query in state`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSearchChanged("rímel")

        assertEquals("rímel", viewModel.uiState.value.query)
    }

    @Test
    fun `onSectionSelected updates section and tracks analytics`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSectionSelected(Section.CURSOS)

        assertEquals(Section.CURSOS, viewModel.uiState.value.selectedSection)
        verify { analytics.track(AnalyticsEvent.SectionSelected(Section.CURSOS.name)) }
    }

    @Test
    fun `toggleFavorite does nothing when there is no logged user`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        // currentUserId es null por defecto, así que debe retornar antes de togglear.
        viewModel.toggleFavorite("item1", FavoriteType.PRODUCT)
        advanceUntilIdle()

        coVerify(exactly = 0) {
            toggleFavoriteUseCase(any(), any(), any(), any())
        }
    }
}
