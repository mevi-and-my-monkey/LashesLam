package com.mevi.lasheslam.domain.usecase.service

import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class DeleteServiceUseCase @Inject constructor(private val repository: ServicesRepository) {
    suspend operator fun invoke(serviceId: String, imageUrl: String) =
        repository.deleteService(serviceId = serviceId, imageUrl = imageUrl)
}