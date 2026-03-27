package com.gilorroristore.cursotestingandroid.cart.presentation

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartSummary
import com.gilorroristore.cursotestingandroid.cart.presentation.model.CartItemsWithPromotion

sealed class CartUiState {
    data class Success(
        val summary: CartSummary,
        val cartItems: List<CartItemsWithPromotion>,
        val isLoading: Boolean
    ) : CartUiState()

    data object Loading : CartUiState()
}