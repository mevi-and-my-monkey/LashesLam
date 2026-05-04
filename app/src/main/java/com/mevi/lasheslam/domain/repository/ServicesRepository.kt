package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.ServiceItem
import kotlinx.coroutines.flow.Flow

interface ServicesRepository {
    fun getCategories(): Flow<Resource<List<CategoryModel>>>
    fun getAllServices(): Flow<Resource<List<ServiceItem>>>
}