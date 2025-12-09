package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.network.CourseRequest
import javax.inject.Inject

class SendRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository
) {
    suspend operator fun invoke(request: CourseRequest) = repo.sendRequest(request)
}

class GetRequestsUseCase @Inject constructor(
    private val repo: CourseRequestRepository
) {
    suspend operator fun invoke(status: String) = repo.getRequestsByStatus(status)
}

class ApproveRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository
) {
    suspend operator fun invoke(id: String) = repo.approveRequest(id)
}

class RejectRequestUseCase @Inject constructor(
    private val repo: CourseRequestRepository
) {
    suspend operator fun invoke(id: String) = repo.rejectRequest(id)
}