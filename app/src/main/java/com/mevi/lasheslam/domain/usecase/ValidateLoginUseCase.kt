package com.mevi.lasheslam.domain.usecase

import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor() {

    operator fun invoke(email: String, password: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        val isEmailValid = emailRegex.matches(email)

        val isPasswordValid = password.length > 6

        return isEmailValid && isPasswordValid
    }
}