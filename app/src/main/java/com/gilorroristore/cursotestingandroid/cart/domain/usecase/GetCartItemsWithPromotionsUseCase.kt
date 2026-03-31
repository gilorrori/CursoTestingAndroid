package com.gilorroristore.cursotestingandroid.cart.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.ex.activeAt
import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.cart.presentation.model.CartItemWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject

class GetCartItemsWithPromotionsUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {

    operator fun invoke(): Flow<List<CartItemWithPromotion>> {

        //flatMapLatest Si hay un map lo cancela y devuelve a consumir y regresa el ultimo
        return cartItemRepository.getCardItems().flatMapLatest { cartItems ->
            val ids = cartItems.mapTo(mutableSetOf()) { it.productId }

            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    productRepository.getProductsByIds(ids),
                    promotionRepository.getActivePromotions()
                ) { products, promotions ->
                    val now = Instant.now()
                    val activePromotions = promotions.activeAt(now)
                    val productsById = products.associateBy { it.id }
                    cartItems.mapNotNull { cartItem ->
                        val product = productsById[cartItem.productId] ?: return@mapNotNull null
                        val promotion = getPromotionForProduct.invoke(product, activePromotions)
                        val productWithPromotion = ProductWithPromotion(product, promotion)

                        CartItemWithPromotion(cartItem = cartItem, item = productWithPromotion)
                    }
                }
            }
        }
    }
}