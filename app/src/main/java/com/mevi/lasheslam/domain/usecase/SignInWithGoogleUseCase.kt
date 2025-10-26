package com.mevi.lasheslam.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(credential: AuthCredential) = repo.signInWithGoogle(credential)
}