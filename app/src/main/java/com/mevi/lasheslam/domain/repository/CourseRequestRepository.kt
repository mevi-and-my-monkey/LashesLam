package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.CourseRequest

interface CourseRequestRepository {
    suspend fun sendRequest(request: CourseRequest): Resource<Boolean>

    suspend fun getRequestsByStatus(status: String): Resource<List<CourseRequest>>

    suspend fun approveRequest(requestId: String): Resource<Boolean>

    suspend fun rejectRequest(requestId: String): Resource<Boolean>
}
