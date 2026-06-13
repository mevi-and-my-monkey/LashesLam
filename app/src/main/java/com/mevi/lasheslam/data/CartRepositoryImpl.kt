package com.mevi.lasheslam.data

import com.mevi.lasheslam.domain.repository.CartRepository
import com.mevi.lasheslam.network.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor() : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val items = _items.asStateFlow()

    override fun addItem(item: CartItem) {
        _items.value = _items.value.toMutableList().apply {
            val index = indexOfFirst { it.productId == item.productId }
            if (index >= 0) {
                this[index] = this[index].copy(quantity = this[index].quantity + item.quantity)
            } else {
                add(item)
            }
        }
    }

    override fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeItem(productId)
            return
        }
        _items.value = _items.value.map {
            if (it.productId == productId) it.copy(quantity = quantity) else it
        }
    }

    override fun removeItem(productId: String) {
        _items.value = _items.value.filterNot { it.productId == productId }
    }

    override fun clear() {
        _items.value = emptyList()
    }
}
