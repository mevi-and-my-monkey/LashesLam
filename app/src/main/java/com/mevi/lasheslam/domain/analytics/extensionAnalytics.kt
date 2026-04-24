package com.mevi.lasheslam.domain.analytics

import com.mevi.lasheslam.core.error.AppError

fun AppError.toAnalyticsType(): String {

    return when (this) {

        AppError.Network -> ErrorType.NETWORK

        AppError.InvalidCredentials -> ErrorType.INVALID_CREDENTIALS

        AppError.UserNotFound -> ErrorType.USER_NOT_FOUND

        is AppError.Unknown -> ErrorType.UNKNOWN

    }

}