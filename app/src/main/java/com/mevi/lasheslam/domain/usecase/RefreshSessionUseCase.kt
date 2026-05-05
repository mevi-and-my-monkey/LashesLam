package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import javax.inject.Inject

class RefreshSessionUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    suspend operator fun invoke() {
        repository.refreshSession()

        val email = repository.getEmail()
        repository.setName()
        if (email != null) {
            val isAdmin = repository.isAdmin(email)
            repository.setAdmin(isAdmin)
            repository.setSessionManager()
        }
    }
}