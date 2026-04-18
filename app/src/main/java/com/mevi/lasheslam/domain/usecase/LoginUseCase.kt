package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<Boolean> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error(AppError.Unknown("Datos invalidos"))
        }
        return repo.signIn(email, password)
    }
}