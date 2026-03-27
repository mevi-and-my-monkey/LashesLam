package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.UpdateResult

interface UpdateRepository {
    suspend fun getUpdateInfo(): UpdateResult
}