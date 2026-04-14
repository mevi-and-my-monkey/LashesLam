package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UpdateRepository
import com.mevi.lasheslam.core.results.UpdateResult
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(private val repository: UpdateRepository) {
    suspend operator fun invoke(): UpdateResult = repository.getUpdateInfo()
}