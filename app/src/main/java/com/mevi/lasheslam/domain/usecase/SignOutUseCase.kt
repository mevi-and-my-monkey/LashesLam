package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke() = repository.signOut()
}
