package com.mevi.lasheslam.core.error

interface ErrorMapper {
    fun map(e: Exception): AppError
}