package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.domain.model.UserModel
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): Resource<UserModel> = repository.getUserProfile()
}
