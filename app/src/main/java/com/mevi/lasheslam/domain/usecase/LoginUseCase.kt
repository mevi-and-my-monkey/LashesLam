package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) = repo.signIn(email, password)
}