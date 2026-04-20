package com.mevi.lasheslam.core.results

import com.mevi.lasheslam.core.error.AppError

sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val error: AppError) : Resource<Nothing>()
}