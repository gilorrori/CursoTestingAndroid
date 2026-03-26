package com.gilorroristore.cursotestingandroid.cart.domain.repositories

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartItemRepository {
    fun getCardItems(): Flow<List<CartItem>>
    suspend fun addToCart(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    suspend fun updateQuantity(productId: String)
    suspend fun clearCart()
}