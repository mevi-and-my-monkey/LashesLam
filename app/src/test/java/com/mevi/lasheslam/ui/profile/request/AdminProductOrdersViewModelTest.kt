package com.mevi.lasheslam.ui.profile.request

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.usecase.cart.GetProductOrdersByStatusUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateProductOrderStatusUseCase
import com.mevi.lasheslam.network.ProductOrder
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AdminProductOrdersViewModelTest {

    private val getProductOrdersByStatusUseCase: GetProductOrdersByStatusUseCase = mockk()
    private val updateProductOrderStatusUseCase: UpdateProductOrderStatusUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() =
        AdminProductOrdersViewModel(getProductOrdersByStatusUseCase, updateProductOrderStatusUseCase)

    @Test
    fun `loadOrders stores orders and counts pending ones`() = runTest {
        val orders = listOf(
            ProductOrder(orderId = "1", status = FirestorePaths.Orders.STATUS_PENDING),
            ProductOrder(orderId = "2", status = FirestorePaths.Orders.STATUS_PENDING),
            ProductOrder(orderId = "3", status = FirestorePaths.Orders.STATUS_COMPLETED)
        )
        coEvery { getProductOrdersByStatusUseCase(any()) } returns Resource.Success(orders)

        val viewModel = buildViewModel()
        viewModel.loadOrders()
        advanceUntilIdle()

        assertEquals(orders, viewModel.orders)
        assertEquals(2, viewModel.pendingCount)
        assertTrue(!viewModel.isLoading)
    }

    @Test
    fun `loadOrders clears list on error`() = runTest {
        coEvery { getProductOrdersByStatusUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        viewModel.loadOrders()
        advanceUntilIdle()

        assertTrue(viewModel.orders.isEmpty())
    }

    @Test
    fun `onFilterSelected updates filter and queries only that status`() = runTest {
        coEvery { getProductOrdersByStatusUseCase(any()) } returns Resource.Success(emptyList())

        val viewModel = buildViewModel()
        viewModel.onFilterSelected(OrderFilter.ARCHIVADO)
        advanceUntilIdle()

        assertEquals(OrderFilter.ARCHIVADO, viewModel.filter)
        coVerify { getProductOrdersByStatusUseCase(listOf(FirestorePaths.Orders.STATUS_ARCHIVED)) }
    }

    @Test
    fun `completeOrder updates status to completed and reloads`() = runTest {
        coEvery { getProductOrdersByStatusUseCase(any()) } returns Resource.Success(emptyList())
        coEvery { updateProductOrderStatusUseCase(any(), any()) } returns Resource.Success(true)

        val viewModel = buildViewModel()
        viewModel.completeOrder("order1")
        advanceUntilIdle()

        coVerify {
            updateProductOrderStatusUseCase("order1", FirestorePaths.Orders.STATUS_COMPLETED)
        }
    }
}
