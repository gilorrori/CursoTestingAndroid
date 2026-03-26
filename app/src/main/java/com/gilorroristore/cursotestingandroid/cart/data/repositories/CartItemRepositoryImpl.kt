package com.gilorroristore.cursotestingandroid.cart.data.repositories

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem
import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.productlist.data.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemRepositoryImpl @Inject constructor(
    val localDataSource: LocalDataSource
) : CartItemRepository {
    override fun getCardItems(): Flow<List<CartItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToCart(productId: String, quantity: Int) {

    }

    override suspend fun removeFromCart(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuantity(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCart() {
        TODO("Not yet implemented")
    }
}