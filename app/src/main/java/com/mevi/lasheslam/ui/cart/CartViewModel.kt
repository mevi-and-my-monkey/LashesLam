package com.mevi.lasheslam.ui.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.usecase.GetUserProfileUseCase
import com.mevi.lasheslam.domain.usecase.UpdateAddressUseCase
import com.mevi.lasheslam.domain.usecase.UpdatePhoneUseCase
import com.mevi.lasheslam.domain.usecase.cart.ClearCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.CreateProductOrderUseCase
import com.mevi.lasheslam.domain.usecase.cart.GetCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.RemoveFromCartUseCase
import com.mevi.lasheslam.domain.usecase.cart.UpdateCartQuantityUseCase
import com.mevi.lasheslam.domain.model.CartItem
import com.mevi.lasheslam.domain.model.DeliveryType
import com.mevi.lasheslam.domain.model.ProductOrder
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
    private val createProductOrderUseCase: CreateProductOrderUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val updatePhoneUseCase: UpdatePhoneUseCase,
    private val sessionDataSource: SessionDataSource
) : ViewModel() {

    val items: StateFlow<List<CartItem>> = getCartUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var isLoading by mutableStateOf(false)
        private set

    var orderPlaced by mutableStateOf<ProductOrder?>(null)
        private set

    var showError by mutableStateOf(false)
        private set

    // Domicilio y teléfono guardados del usuario (para prellenar el checkout)
    var userAddress by mutableStateOf<String?>(null)
        private set

    var userPhone by mutableStateOf<String?>(null)
        private set

    val shippingCost: Double
        get() = sessionDataSource.shippingCost.value

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            when (val result = getUserProfileUseCase()) {
                is Resource.Success -> {
                    userAddress = result.data.address
                    userPhone = result.data.phone
                }

                is Resource.Error -> Unit
            }
        }
    }

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

    fun finalizeOrder(
        deliveryType: DeliveryType,
        address: String,
        phone: String,
        onOpenWhatsApp: (String) -> Unit
    ) {
        val cartItems = items.value
        if (cartItems.isEmpty() || isLoading) return

        viewModelScope.launch {
            isLoading = true

            val isDelivery = deliveryType == DeliveryType.DELIVERY
            val subtotal = cartItems.sumOf { it.price * it.quantity }
            // El costo de envío solo aplica cuando se manda a domicilio
            val shipping = if (isDelivery) shippingCost else 0.0
            val orderAddress = if (isDelivery) address else ""

            // El teléfono es obligatorio en ambos casos; si cambió lo guardamos
            if (phone.isNotBlank() && phone != userPhone) {
                when (val r = updatePhoneUseCase(phone)) {
                    is Resource.Success -> userPhone = phone
                    is Resource.Error -> Log.e(TAG, "updatePhone falló: ${r.error}")
                }
            }

            // Si el usuario capturó un domicilio nuevo, lo guardamos en su perfil
            if (isDelivery && orderAddress.isNotBlank() && orderAddress != userAddress) {
                when (val r = updateAddressUseCase(orderAddress)) {
                    is Resource.Success -> userAddress = orderAddress
                    is Resource.Error -> Log.e(TAG, "updateAddress falló: ${r.error}")
                }
            }

            val order = ProductOrder(
                userId = sessionDataSource.currentUserId.value.orEmpty(),
                nameUser = sessionDataSource.nameUser.value.orEmpty(),
                emailUser = sessionDataSource.email.value.orEmpty(),
                items = cartItems,
                subtotal = subtotal,
                shipping = shipping,
                total = subtotal + shipping,
                deliveryType = deliveryType.value,
                address = orderAddress
            )

            when (val result = createProductOrderUseCase(order)) {
                is Resource.Success -> {
                    clearCartUseCase()
                    orderPlaced = result.data

                    val whatsapp = sessionDataSource.whatsApp.value
                        ?.takeIf { it.isNotEmpty() }
                        ?: Strings.defaultAdminWhatsapp
                    onOpenWhatsApp(Utilities.createOrderMessageWhatsApp(result.data, whatsapp))
                }

                is Resource.Error -> {
                    Log.e(TAG, "createOrder falló: ${result.error}")
                    showError = true
                }
            }
            isLoading = false
        }
    }

    private companion object {
        const val TAG = "CartViewModel"
    }
}
