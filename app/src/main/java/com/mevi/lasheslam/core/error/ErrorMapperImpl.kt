package com.mevi.lasheslam.core.error

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mevi.lasheslam.core.error.ErrorMapper
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor() : ErrorMapper {
    override fun map(e: Exception): AppError {
        return when (e) {
            is FirebaseFirestoreException -> {
                if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
                    AppError.Network
                } else {
                    AppError.Unknown(e.message)
                }
            }

            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_USER_NOT_FOUND" -> AppError.UserNotFound
                    "ERROR_WRONG_PASSWORD" -> AppError.InvalidCredentials
                    else -> AppError.Unknown(e.message)
                }
            }

            else -> AppError.Unknown(e.message)
        }
    }
}