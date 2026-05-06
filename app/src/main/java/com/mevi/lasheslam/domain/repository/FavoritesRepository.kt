package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.FavoriteItem

interface FavoritesRepository {
    suspend fun addToFavorites(userId: String, itemId: String, type: String): Resource<Boolean>
    suspend fun removeFromFavorites(userId: String, itemId: String): Resource<Boolean>
    suspend fun getFavorites(userId: String): Resource<List<FavoriteItem>>
}