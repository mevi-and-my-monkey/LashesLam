package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource

interface FavoritesRepository {
    suspend fun addToFavorites(userId: String, courseId: String): Resource<Boolean>
    suspend fun removeFromFavorites(userId: String, courseId: String): Resource<Boolean>
    suspend fun getFavorites(userId: String): Resource<List<String>>

}