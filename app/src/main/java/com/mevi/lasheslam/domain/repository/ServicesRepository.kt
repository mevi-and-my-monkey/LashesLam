package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.ServiceItem
import kotlinx.coroutines.flow.Flow

interface ServicesRepository {
    suspend fun createService(service: CreateServiceModel): Resource<Unit>
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllServices(): Flow<Resource<List<ServiceItem>>>
}