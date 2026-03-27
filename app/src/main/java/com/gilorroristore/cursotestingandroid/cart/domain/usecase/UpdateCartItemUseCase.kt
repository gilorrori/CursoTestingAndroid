package com.gilorroristore.cursotestingandroid.cart.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.core.domain.model.AppError
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int) {
        if (quantity < 0) {
            throw AppError.Validation.QuantityMustBePositive
        }

        if (quantity == 0) {
            cartItemRepository.removeFromCart(productId)
        }

        val product =
            productRepository.getProductById(productId).first() ?: throw AppError.NotFoundError

        if (quantity > product.stock) {
            AppError.Validation.InsufficientStock(product.stock)
        }

        cartItemRepository.updateQuantity(productId, quantity)
    }
}