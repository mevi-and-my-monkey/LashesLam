package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.di.IoDispatcher
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.network.CourseRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: CourseRequest) = withContext(dispatcher) {
        repo.sendRequest(request)
    }
}

class GetRequestsUseCase @Inject constructor(
    private val repo: CourseRequestRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(status: String) = withContext(dispatcher) {
        repo.getRequestsByStatus(status)
    }
}

class ApproveRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: String) = withContext(dispatcher) {
        repo.approveRequest(id)
    }
}

class RejectRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: String) = withContext(dispatcher) {
        repo.rejectRequest(id)
    }
}