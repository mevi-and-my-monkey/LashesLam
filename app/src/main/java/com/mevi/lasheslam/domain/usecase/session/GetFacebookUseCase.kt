package com.mevi.lasheslam.domain.usecase.session

import com.mevi.lasheslam.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFacebookUseCase @Inject constructor(private val repository: SessionRepository) {
    operator fun invoke(): Flow<String?> {
        return repository.getFacebook()
    }
}