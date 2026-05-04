package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class GetServicesUseCase  @Inject constructor(
    private val repo: ServicesRepository
) {
    operator fun invoke() = repo.getAllServices()
}