package com.mevi.lasheslam.ui.profile.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.usecase.cart.GetProductOrdersByStatusUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateProductOrderStatusUseCase
import com.mevi.lasheslam.network.ProductOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OrderFilter(val label: String) {
    TODOS("Todos"),
    PENDIENTE("Pendiente"),
    COMPLETADO("Completado"),
    ARCHIVADO("Archivado")
}

@HiltViewModel
class AdminProductOrdersViewModel @Inject constructor(
    private val getProductOrdersByStatusUseCase: GetProductOrdersByStatusUseCase,
    private val updateProductOrderStatusUseCase: UpdateProductOrderStatusUseCase
) : ViewModel() {

    var orders by mutableStateOf<List<ProductOrder>>(emptyList())
        private set

    var filter by mutableStateOf(OrderFilter.TODOS)
        private set

    var pendingCount by mutableIntStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onFilterSelected(newFilter: OrderFilter) {
        filter = newFilter
        loadOrders()
    }

    fun loadOrders() = viewModelScope.launch {
        isLoading = true

        val statuses = when (filter) {
            // "Todos" muestra solo pendientes y completadas (sin archivadas)
            OrderFilter.TODOS -> listOf(
                FirestorePaths.Orders.STATUS_PENDING,
                FirestorePaths.Orders.STATUS_COMPLETED,
                FirestorePaths.Orders.STATUS_LEGACY_ACCEPTED
            )

            OrderFilter.PENDIENTE -> listOf(FirestorePaths.Orders.STATUS_PENDING)

            OrderFilter.COMPLETADO -> listOf(
                FirestorePaths.Orders.STATUS_COMPLETED,
                FirestorePaths.Orders.STATUS_LEGACY_ACCEPTED
            )

            OrderFilter.ARCHIVADO -> listOf(FirestorePaths.Orders.STATUS_ARCHIVED)
        }

        when (val result = getProductOrdersByStatusUseCase(statuses)) {
            is Resource.Success -> {
                orders = result.data
                if (statuses.contains(FirestorePaths.Orders.STATUS_PENDING)) {
                    pendingCount = result.data.count {
                        it.status == FirestorePaths.Orders.STATUS_PENDING
                    }
                }
            }

            is Resource.Error -> orders = emptyList()
        }
        isLoading = false
    }

    fun completeOrder(orderId: String) =
        updateStatus(orderId, FirestorePaths.Orders.STATUS_COMPLETED)

    fun archiveOrder(orderId: String) =
        updateStatus(orderId, FirestorePaths.Orders.STATUS_ARCHIVED)

    private fun updateStatus(orderId: String, status: String) = viewModelScope.launch {
        isLoading = true
        updateProductOrderStatusUseCase(orderId, status)
        isLoading = false
        loadOrders()
    }
}
