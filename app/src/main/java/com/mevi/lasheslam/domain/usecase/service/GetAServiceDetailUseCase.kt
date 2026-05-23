package com.mevi.lasheslam.domain.usecase.service

import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class GetAServiceDetailUseCase @Inject constructor(private val repo: ServicesRepository) {
    suspend operator fun invoke(serviceID: String) = repo.getServiceId(serviceID = serviceID)
}