package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIsAdminUseCase @Inject constructor(private val repository: SessionRepository) {
    operator fun invoke(): Flow<Boolean> {
        return repository.getIsAdmin()
    }
}