package com.mevi.lasheslam.domain.usecase.service

import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class GetFavoriteServicesUseCase  @Inject constructor(
    private val repo: ServicesRepository
) {
    suspend operator fun invoke(ids: List<String>) = repo.getServicesByIds(ids)
}