package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import com.mevi.lasheslam.network.FavoriteItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : FavoritesRepository {

    private fun favoritesRef(userId: String) =
        firestore.collection(FirestorePaths.Users.COLLECTION)
            .document(userId)
            .collection(FirestorePaths.Favorites.COLLECTION_FAVORITES)

    override suspend fun addToFavorites(
        userId: String,
        itemId: String,
        type: String
    ): Resource<Boolean> {
        return try {
            favoritesRef(userId)
                .document(itemId)
                .set(
                    mapOf(
                        "itemId" to itemId,
                        "type" to type
                    )
                )
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun removeFromFavorites(
        userId: String,
        itemId: String
    ): Resource<Boolean> {
        return try {

            favoritesRef(userId)
                .document(itemId)
                .delete()
                .await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getFavorites(
        userId: String
    ): Resource<List<FavoriteItem>> {
        return try {
            val snapshot = favoritesRef(userId)
                .get()
                .await()
            val items = snapshot.documents.mapNotNull { doc ->
                val itemId = doc.getString("itemId") ?: return@mapNotNull null
                val type = doc.getString("type") ?: return@mapNotNull null
                FavoriteItem(itemId, type)
            }
            Resource.Success(items)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }
}