package com.gilorroristore.cursotestingandroid.cart.presentation

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartSummary
import com.gilorroristore.cursotestingandroid.cart.presentation.model.CartItemWithPromotion

sealed class CartUiState {
    data class Success(
        val summary: CartSummary,
        val cartItems: List<CartItemWithPromotion>,
        val isLoading: Boolean
    ) : CartUiState()

    data class Error(val message: String) : CartUiState()
    data object Loading : CartUiState()
}