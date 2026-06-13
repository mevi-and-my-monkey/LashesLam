package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.FavoriteItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addToFavorites(userId: String, itemId: String, type: String): Resource<Boolean>
    suspend fun removeFromFavorites(userId: String, itemId: String): Resource<Boolean>
    suspend fun getFavorites(userId: String): Resource<List<FavoriteItem>>
    fun observeFavorites(userId: String): Flow<List<FavoriteItem>>
}