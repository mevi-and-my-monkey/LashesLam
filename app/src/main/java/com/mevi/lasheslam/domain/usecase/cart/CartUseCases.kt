package com.mevi.lasheslam.domain.usecase.cart

import com.mevi.lasheslam.domain.repository.CartRepository
import com.mevi.lasheslam.network.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(): Flow<List<CartItem>> = repository.items
}

class AddToCartUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(item: CartItem) = repository.addItem(item)
}

class UpdateCartQuantityUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(productId: String, quantity: Int) =
        repository.updateQuantity(productId, quantity)
}

class RemoveFromCartUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(productId: String) = repository.removeItem(productId)
}

class ClearCartUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke() = repository.clear()
}
