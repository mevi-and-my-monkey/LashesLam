package com.mevi.lasheslam.ui.products

import android.net.Uri
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateProductUseCase
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.ObserveFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.domain.usecase.cart.AddToCartUseCase
import com.mevi.lasheslam.domain.usecase.products.DeleteProductUseCase
import com.mevi.lasheslam.domain.usecase.products.GetAProductDetailUseCase
import com.mevi.lasheslam.domain.usecase.products.UpdateProductUseCase
import com.mevi.lasheslam.domain.usecase.session.GetEmailUserUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductsViewModelTest {

    private val getIsAdminUseCase: GetIsAdminUseCase = mockk(relaxed = true)
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase = mockk(relaxed = true)
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = mockk(relaxed = true)
    private val getNameUserUseCase: GetNameUserUseCase = mockk(relaxed = true)
    private val getEmailUserUseCase: GetEmailUserUseCase = mockk(relaxed = true)
    private val observeFavoritesUseCase: ObserveFavoritesUseCase = mockk(relaxed = true)
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)
    private val getFacebookUseCase: GetFacebookUseCase = mockk(relaxed = true)
    private val getInstagramUseCase: GetInstagramUseCase = mockk(relaxed = true)
    private val getWhatsAppUseCase: GetWhatsAppUseCase = mockk(relaxed = true)
    private val getAProductDetailUseCase: GetAProductDetailUseCase = mockk(relaxed = true)
    private val getCategoriesProducts: GetCategoriesProducts = mockk(relaxed = true)
    private val createProductUseCase: CreateProductUseCase = mockk()
    private val deleteProductUseCase: DeleteProductUseCase = mockk(relaxed = true)
    private val updateProductUseCase: UpdateProductUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val analytics: AnalyticsTracker = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = ProductsViewModel(
        getIsAdminUseCase,
        getIsUserInvitedUseCase,
        getCurrentUserIdUseCase,
        getNameUserUseCase,
        getEmailUserUseCase,
        observeFavoritesUseCase,
        toggleFavoriteUseCase,
        getFacebookUseCase,
        getInstagramUseCase,
        getWhatsAppUseCase,
        getAProductDetailUseCase,
        getCategoriesProducts,
        createProductUseCase,
        deleteProductUseCase,
        updateProductUseCase,
        addToCartUseCase,
        analytics
    )

    @Test
    fun `form change handlers update the form`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onTitleChange("Rímel")
        viewModel.onCostChange("199")

        assertEquals("Rímel", viewModel.uiState.value.form.titulo)
        assertEquals("199", viewModel.uiState.value.form.precio)
    }

    @Test
    fun `onImagesSelected keeps at most five distinct images`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        val images = List(6) { mockk<Uri>() }
        viewModel.onImagesSelected(images)

        assertEquals(5, viewModel.uiState.value.form.images.size)
    }

    @Test
    fun `addToCart forwards the selected product to the cart use case`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.addToCart("p1", 2)

        verify { addToCartUseCase(match { it.productId == "p1" && it.quantity == 2 }) }
    }

    @Test
    fun `saveProduct emits ProductSaved on success`() = runTest {
        coEvery { createProductUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = buildViewModel()
        val events = mutableListOf<ProductUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveProduct()
        advanceUntilIdle()

        assertTrue(events.contains(ProductUiEvent.ProductSaved))
        job.cancel()
    }

    @Test
    fun `saveProduct emits ShowError on failure`() = runTest {
        coEvery { createProductUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        val events = mutableListOf<ProductUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveProduct()
        advanceUntilIdle()

        assertTrue(events.any { it is ProductUiEvent.ShowError })
        job.cancel()
    }

    @Test
    fun `toggleFavorite does nothing without a logged user`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.toggleFavorite("item1", FavoriteType.PRODUCT)
        advanceUntilIdle()

        coVerify(exactly = 0) { toggleFavoriteUseCase(any(), any(), any(), any()) }
    }
}
