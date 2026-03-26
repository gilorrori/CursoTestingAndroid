package com.gilorroristore.cursotestingandroid.cart.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.core.domain.model.AppError
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(productId: String, quantity: Int = 1) {
        if (quantity <= 0) {
            throw AppError.Validation.QuantityMustBePositive
        }

        val product =
            productRepository.getProductById(productId).first() ?: throw AppError.NotFoundError

        val existingItem = cartItemRepository.getCartItemById(productId)
        val newQuantity = (existingItem?.quantity ?: 0) + quantity
        if (newQuantity > product.stock) {
            throw AppError.Validation.InsufficientStock(product.stock)
        }

        cartItemRepository.addToCart(productId, quantity)
    }
}