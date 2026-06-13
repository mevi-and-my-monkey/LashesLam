package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.ProductOrder

interface ProductOrderRepository {

    suspend fun createOrder(order: ProductOrder): Resource<ProductOrder>

    suspend fun getOrdersByUser(userId: String): Resource<List<ProductOrder>>

    suspend fun getOrdersByStatus(statuses: List<String>): Resource<List<ProductOrder>>

    suspend fun updateStatus(orderId: String, status: String): Resource<Boolean>
}
