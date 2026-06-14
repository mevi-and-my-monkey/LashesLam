package com.mevi.lasheslam.ui.home

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.notifications.ObserveUserCoursesUseCase
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import com.mevi.lasheslam.domain.usecase.GetCategoriesServices
import com.mevi.lasheslam.domain.usecase.GetCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.GetProductsUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.GetServicesUseCase
import com.mevi.lasheslam.domain.usecase.HandleCourseNotificationsUseCase
import com.mevi.lasheslam.domain.usecase.ObserveFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.domain.usecase.booking.HandleReservationNotificationsUseCase
import com.mevi.lasheslam.domain.usecase.booking.ObserveScheduledReservationsUseCase
import com.mevi.lasheslam.domain.usecase.cart.GetCartUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.network.CartItem
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CourseRequest
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomePageViewModelTest {

    private val analytics: AnalyticsTracker = mockk(relaxed = true)
    private val getACoursesUseCase: GetCoursesUseCase = mockk(relaxed = true)
    private val getIsAdminUseCase: GetIsAdminUseCase = mockk(relaxed = true)
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase = mockk(relaxed = true)
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = mockk(relaxed = true)
    private val getRequestsUseCase: GetRequestsUseCase = mockk()
    private val observeUserCoursesUseCase: ObserveUserCoursesUseCase = mockk(relaxed = true)
    private val handleCourseNotificationsUseCase: HandleCourseNotificationsUseCase = mockk(relaxed = true)
    private val getNameUserUseCase: GetNameUserUseCase = mockk(relaxed = true)
    private val getPhotoUserUseCase: GetPhotoUserUseCase = mockk(relaxed = true)
    private val getCategoriesProducts: GetCategoriesProducts = mockk(relaxed = true)
    private val getProductsUseCase: GetProductsUseCase = mockk(relaxed = true)
    private val getCategoriesServices: GetCategoriesServices = mockk(relaxed = true)
    private val getServicesUseCase: GetServicesUseCase = mockk(relaxed = true)
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)
    private val observeFavoritesUseCase: ObserveFavoritesUseCase = mockk(relaxed = true)
    private val getFacebookUseCase: GetFacebookUseCase = mockk(relaxed = true)
    private val getInstagramUseCase: GetInstagramUseCase = mockk(relaxed = true)
    private val getWhatsAppUseCase: GetWhatsAppUseCase = mockk(relaxed = true)
    private val getCartUseCase: GetCartUseCase = mockk()
    private val observeScheduledReservationsUseCase: ObserveScheduledReservationsUseCase = mockk(relaxed = true)
    private val handleReservationNotificationsUseCase: HandleReservationNotificationsUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel(cart: List<CartItem> = emptyList()): HomePageViewModel {
        every { getCartUseCase() } returns flowOf(cart)
        return HomePageViewModel(
            analytics, getACoursesUseCase, getIsAdminUseCase, getIsUserInvitedUseCase,
            getCurrentUserIdUseCase, getRequestsUseCase, observeUserCoursesUseCase,
            handleCourseNotificationsUseCase, getNameUserUseCase, getPhotoUserUseCase,
            getCategoriesProducts, getProductsUseCase, getCategoriesServices, getServicesUseCase,
            toggleFavoriteUseCase, observeFavoritesUseCase, getFacebookUseCase, getInstagramUseCase,
            getWhatsAppUseCase, getCartUseCase, observeScheduledReservationsUseCase,
            handleReservationNotificationsUseCase
        )
    }

    @Test
    fun `observeCart sums the quantities into cartCount`() = runTest {
        val viewModel = buildViewModel(
            cart = listOf(
                CartItem(productId = "a", quantity = 2),
                CartItem(productId = "b", quantity = 3)
            )
        )
        advanceUntilIdle()

        assertEquals(5, viewModel.uiState.value.cartCount)
    }

    @Test
    fun `loadAdminPendingRequests stores the pending count`() = runTest {
        coEvery { getRequestsUseCase(any()) } returns
            Resource.Success(listOf(CourseRequest(), CourseRequest()))

        val viewModel = buildViewModel()
        viewModel.loadAdminPendingRequests()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.adminPendingCount)
    }

    @Test
    fun `loadAdminPendingRequests resets count to zero on error`() = runTest {
        coEvery { getRequestsUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        viewModel.loadAdminPendingRequests()
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.adminPendingCount)
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
    fun `onCategorySelected updates selected category and tracks`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCategorySelected(CategoryModel(id = "cat1", name = "Pestañas"))

        assertEquals("cat1", viewModel.selectedCategoryId)
        verify { analytics.track(AnalyticsEvent.CategorySelected("Pestañas")) }
    }

    @Test
    fun `toggleFavorite does nothing without a logged user`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.toggleFavorite("item1", FavoriteType.COURSE)
        advanceUntilIdle()

        coVerify(exactly = 0) { toggleFavoriteUseCase(any(), any(), any(), any()) }
    }
}
