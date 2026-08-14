package com.mevi.lasheslam.ui.profile.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.OrderStatus
import com.mevi.lasheslam.domain.usecase.cart.GetProductOrdersByStatusUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateProductOrderStatusUseCase
import com.mevi.lasheslam.domain.usecase.products.DecrementStockUseCase
import com.mevi.lasheslam.domain.model.ProductOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OrderFilter(val label: String) {
    TODOS("Todos"),
    PENDIENTE("Pendiente"),
    EN_PROCESO("En proceso"),
    COMPLETADO("Completado"),
    ARCHIVADO("Archivado")
}

@HiltViewModel
class AdminProductOrdersViewModel @Inject constructor(
    private val getProductOrdersByStatusUseCase: GetProductOrdersByStatusUseCase,
    private val updateProductOrderStatusUseCase: UpdateProductOrderStatusUseCase,
    private val decrementStockUseCase: DecrementStockUseCase
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
            // "Todos" muestra activas (sin archivadas)
            OrderFilter.TODOS -> listOf(
                OrderStatus.PENDING.value,
                OrderStatus.PREPARING.value,
                OrderStatus.SHIPPED.value,
                OrderStatus.DELIVERED.value,
                OrderStatus.COMPLETED.value,
                OrderStatus.LEGACY_ACCEPTED.value
            )

            OrderFilter.PENDIENTE -> listOf(OrderStatus.PENDING.value)

            // Envíos en curso
            OrderFilter.EN_PROCESO -> listOf(
                OrderStatus.PREPARING.value,
                OrderStatus.SHIPPED.value
            )

            OrderFilter.COMPLETADO -> listOf(
                OrderStatus.COMPLETED.value,
                OrderStatus.DELIVERED.value,
                OrderStatus.LEGACY_ACCEPTED.value
            )

            OrderFilter.ARCHIVADO -> listOf(OrderStatus.ARCHIVED.value)
        }

        when (val result = getProductOrdersByStatusUseCase(statuses)) {
            is Resource.Success -> {
                orders = result.data
                if (statuses.contains(OrderStatus.PENDING.value)) {
                    pendingCount = result.data.count {
                        it.status == OrderStatus.PENDING.value
                    }
                }
            }

            is Resource.Error -> orders = emptyList()
        }
        isLoading = false
    }

    /**
     * Recoger en tienda: el admin marca el pedido como finalizado. Aquí se
     * descuenta el stock (transición pendiente → finalizado).
     */
    fun completeOrder(order: ProductOrder) = viewModelScope.launch {
        isLoading = true
        decrementStockUseCase(order.items)
        updateProductOrderStatusUseCase(order.orderId, OrderStatus.COMPLETED.value)
        isLoading = false
        loadOrders()
    }

    /**
     * Envío a domicilio: el admin acepta y empieza a preparar. Aquí se descuenta
     * el stock (transición pendiente → preparando).
     */
    fun startDelivery(order: ProductOrder) = viewModelScope.launch {
        isLoading = true
        decrementStockUseCase(order.items)
        updateProductOrderStatusUseCase(order.orderId, OrderStatus.PREPARING.value)
        isLoading = false
        loadOrders()
    }

    fun markShipped(orderId: String) =
        updateStatus(orderId, OrderStatus.SHIPPED.value)

    fun markDelivered(orderId: String) =
        updateStatus(orderId, OrderStatus.DELIVERED.value)

    fun archiveOrder(orderId: String) =
        updateStatus(orderId, OrderStatus.ARCHIVED.value)

    private fun updateStatus(orderId: String, status: String) = viewModelScope.launch {
        isLoading = true
        updateProductOrderStatusUseCase(orderId, status)
        isLoading = false
        loadOrders()
    }
}
