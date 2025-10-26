package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.network.UserModel
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(user: UserModel) = repo.register(user)
}