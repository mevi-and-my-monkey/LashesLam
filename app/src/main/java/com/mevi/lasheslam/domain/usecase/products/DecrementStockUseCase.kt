package com.mevi.lasheslam.domain.usecase.products

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CartItem
import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject

/**
 * Descuenta el stock de los productos de un pedido. Se invoca al aceptar
 * (finalizar) la orden. Los productos sin stock gestionado (null) se ignoran.
 */
class DecrementStockUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(items: List<CartItem>): Resource<Unit> =
        repository.decrementStock(items)
}
