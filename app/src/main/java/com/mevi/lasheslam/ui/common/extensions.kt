package com.mevi.lasheslam.ui.common

import com.mevi.lasheslam.core.error.AppError

fun AppError.toUserMessage(): String {

    return when (this) {

        AppError.Network -> "Sin conexión a internet"

        AppError.InvalidCredentials -> "Correo o contraseña incorrectos"

        AppError.UserNotFound -> "Usuario no encontrado"

        is AppError.Unknown -> {
            message ?: "Ocurrió un error, intenta nuevamente"
        }

    }

}

fun String?.orDefault(default: String) = if (this.isNullOrEmpty()) default else this