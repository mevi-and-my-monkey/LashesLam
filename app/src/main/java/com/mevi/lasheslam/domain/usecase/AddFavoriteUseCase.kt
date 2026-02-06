package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String,
        courseId: String
    ) = repo.addToFavorites(userId, courseId)
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

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend operator fun invoke(
        userId: String,
        courseId: String,
        isFavorite: Boolean
    ): Resource<Boolean> {
        return if (isFavorite) {
            repo.removeFromFavorites(userId, courseId)
        } else {
            repo.addToFavorites(userId, courseId)
        }
    }
}