package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CreateServiceDto
import com.mevi.lasheslam.network.ServiceItem
import kotlinx.coroutines.flow.Flow

interface ServicesRepository {
    suspend fun createService(service: CreateServiceModel): Resource<Unit>
    suspend fun getServiceId(serviceID: String): Resource<CreateServiceDto>
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllServices(): Flow<Resource<List<ServiceItem>>>
    suspend fun deleteService(serviceId: String, imageUrl: String): Resource<Unit>
    suspend fun updateService(service: CreateServiceModel): Resource<Unit>
}