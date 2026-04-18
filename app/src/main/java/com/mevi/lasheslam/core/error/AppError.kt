package com.mevi.lasheslam.core.error

sealed class AppError {
    object Network : AppError()
    object InvalidCredentials : AppError()
    object UserNotFound : AppError()
    data class Unknown(val message: String?) : AppError()
}