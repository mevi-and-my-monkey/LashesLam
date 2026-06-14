package com.mevi.lasheslam.ui.cart

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.cart.ClearCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.CreateProductOrderUseCase
import com.mevi.lasheslam.domain.usecase.cart.GetCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.RemoveFromCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateCartQuantityUseCase
import com.mevi.lasheslam.network.CartItem
import com.mevi.lasheslam.network.ProductOrder
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    private val getCartUseCase: GetCartUseCase = mockk()
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase = mockk(relaxed = true)
    private val removeFromCartUseCase: RemoveFromCartUseCase = mockk(relaxed = true)
    private val clearCartUseCase: ClearCartUseCase = mockk(relaxed = true)
    private val createProductOrderUseCase: CreateProductOrderUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sampleItems = listOf(
        CartItem(productId = "p1", title = "Rímel", price = 100.0, quantity = 2)
    )

    private fun buildViewModel(items: List<CartItem> = sampleItems): CartViewModel {
        every { getCartUseCase() } returns flowOf(items)
        return CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            removeFromCartUseCase,
            clearCartUseCase,
            createProductOrderUseCase
        )
    }

    @Test
    fun `updateQuantity delegates to use case`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.updateQuantity("p1", 3)

        verify { updateCartQuantityUseCase("p1", 3) }
    }

    @Test
    fun `removeItem delegates to use case`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.removeItem("p1")

        verify { removeFromCartUseCase("p1") }
    }

    @Test
    fun `finalizeOrder does nothing when cart is empty`() = runTest {
        val viewModel = buildViewModel(items = emptyList())
        advanceUntilIdle()

        var whatsappOpened = false
        viewModel.finalizeOrder { whatsappOpened = true }
        advanceUntilIdle()

        assertFalse(whatsappOpened)
        coVerify(exactly = 0) { createProductOrderUseCase(any()) }
    }

    @Test
    fun `finalizeOrder success clears cart, stores order and opens whatsapp`() = runTest {
        val placedOrder = ProductOrder(orderNumber = "1001")
        coEvery { createProductOrderUseCase(any()) } returns Resource.Success(placedOrder)

        val viewModel = buildViewModel()
        advanceUntilIdle()

        var url: String? = null
        viewModel.finalizeOrder { url = it }
        advanceUntilIdle()

        assertEquals(placedOrder, viewModel.orderPlaced)
        verify { clearCartUseCase() }
        assertTrue(url?.startsWith("https://wa.me/") == true)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `finalizeOrder error flags showError and keeps order null`() = runTest {
        coEvery { createProductOrderUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.finalizeOrder { }
        advanceUntilIdle()

        assertTrue(viewModel.showError)
        assertNull(viewModel.orderPlaced)
        assertFalse(viewModel.isLoading)

        viewModel.clearError()
        assertFalse(viewModel.showError)
    }
}
