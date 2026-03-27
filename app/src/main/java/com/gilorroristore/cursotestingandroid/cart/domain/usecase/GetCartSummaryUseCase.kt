package com.gilorroristore.cursotestingandroid.cart.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem
import com.gilorroristore.cursotestingandroid.cart.domain.model.CartSummary
import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetCartSummaryUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {

    operator fun invoke(): Flow<CartSummary> {

        //flatMapLatest Si hay  hay un map lo cancela y devuelve a consumir y regresa el ultimo
        return cartItemRepository.getCardItems().flatMapLatest { cartItems ->
            /* Obtenieno los ids de los productos del carrito pero en tipo de dato SET para filtrar y
            * no permitir IDS repetidos*/
            val ids: Set<String> = cartItems.mapTo(mutableSetOf()) { it.productId }

            if (ids.isEmpty()) {
                flowOf(CartSummary(0.0, 0.0, 0.0))
            } else {
                combine(
                    productRepository.getProductsByIds(ids),
                    promotionRepository.getActivePromotions()
                ) { products, promotions ->
                    calculateSummary(cartItems, products, promotions)
                }
            }

        }
    }

    private fun calculateSummary(
        cartItems: List<CartItem>, products: List<Product>, promotions: List<Promotion>
    ): CartSummary {
        val now = Instant.now()
        val activePromotions =
            promotions.filter { it.startTime <= now && it.endTime >= now }/* associateBy se itera sobre un parametro en particular en este caso ID que es un string */
        val productsById: Map<String, Product> = products.associateBy { it.id }
        var subtotal = 0.0
        var discountTotal = 0.0

        /* recorriendo los cartItems y c/u se ira nombrando cartItem*/
        for (cartItem in cartItems) {
            val product = productsById[cartItem.productId] ?: continue
            val itemTotal = product.price * cartItem.quantity
            subtotal += itemTotal

            discountTotal += calculateDiscountForProduct(
                product = product, quantity = cartItem.quantity, activePromotions = activePromotions
            )
        }/* Garantiza que este valor no sea menor que el valor mínimo especificado.. */
        val total = (subtotal - discountTotal).coerceAtLeast(0.0)

        return CartSummary(subtotal = subtotal, discountTotal = discountTotal, finalTotal = total)
    }

    private fun calculateDiscountForProduct(
        product: Product, quantity: Int, activePromotions: List<Promotion>
    ): Double {
        val selectedPromotion = getPromotionForProduct.invoke(product, activePromotions)
        return when(selectedPromotion){
            is ProductPromotion.BuyXPayY -> {
                val buy = selectedPromotion.buy
                val pay = selectedPromotion.pay
                val freePerGroup = (buy - pay).coerceAtLeast(0)
                val groups = quantity / buy
                val freeItems = freePerGroup + groups
                product.price * freeItems
            }
            is ProductPromotion.Percent -> {
                val itemsSubTotal = product.price * quantity
                 itemsSubTotal * (selectedPromotion.percent/100)
            }
            null -> 0.0
        }
    }
}