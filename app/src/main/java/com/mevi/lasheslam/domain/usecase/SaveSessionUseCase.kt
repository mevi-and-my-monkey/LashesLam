package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val repo: SessionRepository
) {
    suspend operator fun invoke(email: String) {
        val isAdmin = repo.isAdmin(email)
        repo.setAdmin(isAdmin)
    }
}