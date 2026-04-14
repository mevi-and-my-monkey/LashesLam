package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    operator fun invoke(): Boolean = repository.isLoggedIn()
}