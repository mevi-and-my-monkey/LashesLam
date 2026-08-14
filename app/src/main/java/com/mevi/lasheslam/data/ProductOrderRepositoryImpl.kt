package com.mevi.lasheslam.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.ProductOrderRepository
import com.mevi.lasheslam.domain.model.ProductOrder
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class ProductOrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val errorMapper: ErrorMapper
) : ProductOrderRepository {

    private val ordersRef = firestore.collection(FirestorePaths.Orders.COLLECTION)

    private companion object {
        const val TAG = "ProductOrderRepo"
    }

    override suspend fun createOrder(order: ProductOrder): Resource<ProductOrder> {
        return try {
            // El userId se toma de FirebaseAuth para que coincida con request.auth.uid
            // en las reglas de Firestore (la sesión en memoria puede estar desfasada).
            val uid = firebaseAuth.currentUser?.uid
            if (uid.isNullOrEmpty()) {
                Log.e(TAG, "createOrder: sesión no válida (currentUser nulo)")
                return Resource.Error(errorMapper.map(Exception("Sesión no válida")))
            }

            val doc = ordersRef.document()
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val newOrder = order.copy(
                orderId = doc.id,
                orderNumber = "LL-$year-${doc.id.takeLast(4).uppercase()}",
                userId = uid,
                status = FirestorePaths.Orders.STATUS_PENDING
            )

            doc.set(newOrder).await()

            Resource.Success(newOrder)
        } catch (e: Exception) {
            Log.e(TAG, "createOrder falló: ${e.message}", e)
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getOrdersByUser(userId: String): Resource<List<ProductOrder>> {
        return try {
            val snapshot = ordersRef
                .whereEqualTo(FirestorePaths.Orders.USER_ID, userId)
                .get()
                .await()

            val list = snapshot.documents
                .mapNotNull { it.toObject(ProductOrder::class.java) }
                .sortedByDescending { it.timestamp }

            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun getOrdersByStatus(statuses: List<String>): Resource<List<ProductOrder>> {
        return try {
            val snapshot = ordersRef
                .whereIn(FirestorePaths.Orders.STATUS, statuses)
                .get()
                .await()

            val list = snapshot.documents
                .mapNotNull { it.toObject(ProductOrder::class.java) }
                .sortedByDescending { it.timestamp }

            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }

    override suspend fun updateStatus(orderId: String, status: String): Resource<Boolean> {
        return try {
            ordersRef.document(orderId)
                .update(FirestorePaths.Orders.STATUS, status)
                .await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorMapper.map(e))
        }
    }
}
