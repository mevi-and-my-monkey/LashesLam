package com.mevi.lasheslam.data

import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.ProductOrderRepository
import com.mevi.lasheslam.network.ProductOrder
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class ProductOrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : ProductOrderRepository {

    private val ordersRef = firestore.collection(FirestorePaths.Orders.COLLECTION)

    override suspend fun createOrder(order: ProductOrder): Resource<ProductOrder> {
        return try {
            val doc = ordersRef.document()
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val newOrder = order.copy(
                orderId = doc.id,
                orderNumber = "LL-$year-${doc.id.takeLast(4).uppercase()}",
                status = FirestorePaths.Orders.STATUS_PENDING
            )

            doc.set(newOrder).await()

            Resource.Success(newOrder)
        } catch (e: Exception) {
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
