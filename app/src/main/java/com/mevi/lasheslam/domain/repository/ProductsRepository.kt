package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CreateProductDto
import com.mevi.lasheslam.network.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun createProduct(product: CreateProductModel): Resource<Unit>
    suspend fun getProductById(productId: String): Resource<CreateProductDto>
    suspend fun getProductsByIds(ids: List<String>): Resource<List<ProductItem>>
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllProducts(): Flow<Resource<List<ProductItem>>>
    suspend fun deleteCourse(productId: String, imageUrl: List<String>): Resource<Unit>
    suspend fun updateProduct(product: CreateProductModel): Resource<Unit>
}