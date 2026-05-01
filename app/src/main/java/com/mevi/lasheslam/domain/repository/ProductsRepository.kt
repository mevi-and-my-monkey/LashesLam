package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.CategoryModel
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
}