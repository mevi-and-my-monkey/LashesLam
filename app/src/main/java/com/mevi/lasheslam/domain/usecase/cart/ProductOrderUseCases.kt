package com.mevi.lasheslam.domain.usecase.cart

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.ProductOrderRepository
import com.mevi.lasheslam.network.ProductOrder
import javax.inject.Inject

class CreateProductOrderUseCase @Inject constructor(
    private val repository: ProductOrderRepository
) {
    suspend operator fun invoke(order: ProductOrder): Resource<ProductOrder> =
        repository.createOrder(order)
}

class GetUserProductOrdersUseCase @Inject constructor(
    private val repository: ProductOrderRepository
) {
    suspend operator fun invoke(userId: String): Resource<List<ProductOrder>> =
        repository.getOrdersByUser(userId)
}

class GetProductOrdersByStatusUseCase @Inject constructor(
    private val repository: ProductOrderRepository
) {
    suspend operator fun invoke(statuses: List<String>): Resource<List<ProductOrder>> =
        repository.getOrdersByStatus(statuses)
}

class UpdateProductOrderStatusUseCase @Inject constructor(
    private val repository: ProductOrderRepository
) {
    suspend operator fun invoke(orderId: String, status: String): Resource<Boolean> =
        repository.updateStatus(orderId, status)
}
