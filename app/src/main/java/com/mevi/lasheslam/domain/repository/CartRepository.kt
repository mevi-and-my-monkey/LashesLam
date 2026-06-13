package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.network.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val items: Flow<List<CartItem>>
    fun addItem(item: CartItem)
    fun updateQuantity(productId: String, quantity: Int)
    fun removeItem(productId: String)
    fun clear()
}
