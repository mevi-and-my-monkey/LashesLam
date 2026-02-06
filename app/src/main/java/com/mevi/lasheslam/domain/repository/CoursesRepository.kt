package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.ServiceItem

interface CoursesRepository {
    suspend fun getCoursesByIds(ids: List<String>): Resource<List<ServiceItem>>
}