package com.mevi.lasheslam.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.cart.ClearCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.CreateProductOrderUseCase
import com.mevi.lasheslam.domain.usecase.cart.GetCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.RemoveFromCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateCartQuantityUseCase
import com.mevi.lasheslam.network.CartItem
import com.mevi.lasheslam.network.ProductOrder
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.utils.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getCartUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createProductOrderUseCase: CreateProductOrderUseCase
) : ViewModel() {

    val items: StateFlow<List<CartItem>> = getCartUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var isLoading by mutableStateOf(false)
        private set

    var orderPlaced by mutableStateOf<ProductOrder?>(null)
        private set

    var showError by mutableStateOf(false)
        private set

    val shippingCost: Double
        get() = SessionManager.shippingCost.value

    fun updateQuantity(productId: String, quantity: Int) {
        updateCartQuantityUseCase(productId, quantity)
    }

    fun removeItem(productId: String) {
        removeFromCartUseCase(productId)
    }

    fun clearError() {
        showError = false
    }

    fun resetOrder() {
        orderPlaced = null
    }

    fun finalizeOrder(onOpenWhatsApp: (String) -> Unit) {
        val cartItems = items.value
        if (cartItems.isEmpty() || isLoading) return

        viewModelScope.launch {
            isLoading = true

            val subtotal = cartItems.sumOf { it.price * it.quantity }
            val shipping = shippingCost
            val order = ProductOrder(
                userId = SessionManager.currentUserId.value.orEmpty(),
                nameUser = SessionManager.nameUser.value.orEmpty(),
                emailUser = SessionManager.emailUser.value.orEmpty(),
                items = cartItems,
                subtotal = subtotal,
                shipping = shipping,
                total = subtotal + shipping
            )

            when (val result = createProductOrderUseCase(order)) {
                is Resource.Success -> {
                    clearCartUseCase()
                    orderPlaced = result.data

                    val whatsapp = SessionManager.whatsApp.value
                        ?.takeIf { it.isNotEmpty() }
                        ?: Strings.defaultAdminWhatsapp
                    onOpenWhatsApp(Utilities.createOrderMessageWhatsApp(result.data, whatsapp))
                }

                is Resource.Error -> {
                    showError = true
                }
            }
            isLoading = false
        }
    }
}
