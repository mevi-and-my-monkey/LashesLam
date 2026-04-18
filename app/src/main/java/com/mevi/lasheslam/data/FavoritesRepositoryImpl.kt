package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : FavoritesRepository {

    private fun favoritesRef(userId: String) =
        firestore.collection("users")
            .document(userId)
            .collection("favorites")

    override suspend fun addToFavorites(
        userId: String,
        courseId: String
    ): Resource<Boolean> {
        return try {

            favoritesRef(userId)
                .document(courseId)
                .set(mapOf("courseId" to courseId))
                .await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun removeFromFavorites(
        userId: String,
        courseId: String
    ): Resource<Boolean> {
        return try {

            favoritesRef(userId)
                .document(courseId)
                .delete()
                .await()

            Resource.Success(true)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getFavorites(
        userId: String
    ): Resource<List<String>> {
        return try {

            val snapshot = favoritesRef(userId)
                .get()
                .await()

            val ids = snapshot.documents
                .mapNotNull { it.getString("courseId") }

            Resource.Success(ids)

        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }
}