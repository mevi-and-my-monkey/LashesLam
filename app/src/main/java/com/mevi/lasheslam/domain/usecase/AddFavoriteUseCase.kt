package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String,
        itemId: String,
        type: String,
    ) = repo.addToFavorites(userId = userId, itemId = itemId, type = type)
}

class RemoveFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String,
        courseId: String
    ) = repo.removeFromFavorites(userId, courseId)
}

class GetFavoritesUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String
    ) = repo.getFavorites(userId)
}

class ObserveFavoritesUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    operator fun invoke(userId: String) = repo.observeFavorites(userId)
}

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String,
        itemId: String,
        type: String,
        isFavorite: Boolean
    ): Resource<Boolean> {
        return if (isFavorite) {
            repo.removeFromFavorites(userId, itemId)
        } else {
            repo.addToFavorites(userId, itemId, type)
        }
    }
}