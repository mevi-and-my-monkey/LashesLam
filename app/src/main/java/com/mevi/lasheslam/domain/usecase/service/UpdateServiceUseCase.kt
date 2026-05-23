package com.mevi.lasheslam.domain.usecase.service

import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class UpdateServiceUseCase @Inject constructor(private val repository: ServicesRepository) {
    suspend operator fun invoke(service: CreateServiceModel) = repository.updateService(service)
}