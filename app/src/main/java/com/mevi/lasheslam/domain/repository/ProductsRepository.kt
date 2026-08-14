package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CartItem
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.model.CategoryModel
import com.mevi.lasheslam.domain.model.ProductDetail
import com.mevi.lasheslam.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun createProduct(product: CreateProductModel): Resource<Unit>
    suspend fun getProductById(productId: String): Resource<ProductDetail>
    suspend fun getProductsByIds(ids: List<String>): Resource<List<ProductItem>>
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllProducts(): Flow<Resource<List<ProductItem>>>
    suspend fun deleteCourse(productId: String, imageUrl: List<String>): Resource<Unit>
    suspend fun updateProduct(product: CreateProductModel): Resource<Unit>

    /**
     * Descuenta del stock la cantidad comprada de cada producto. Los productos
     * cuyo campo stock sea null (no gestionado) se omiten.
     */
    suspend fun decrementStock(items: List<CartItem>): Resource<Unit>
}