package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CreateCourseDto
import com.mevi.lasheslam.network.CreateProductDto
import com.mevi.lasheslam.network.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun createProduct(product: CreateProductModel): Resource<Unit>
    suspend fun getProductById(productId: String): Resource<CreateProductDto>
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllProducts(): Flow<Resource<List<ProductItem>>>

}