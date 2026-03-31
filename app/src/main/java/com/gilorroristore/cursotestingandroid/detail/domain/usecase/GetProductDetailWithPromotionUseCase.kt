package com.gilorroristore.cursotestingandroid.detail.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.ex.activeAt
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import javax.inject.Inject

class GetProductDetailWithPromotionUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {
    operator fun invoke(productId: String): Flow<ProductWithPromotion?> {
        return combine(
            productRepository.getProductById(productId),
            promotionRepository.getActivePromotions()
        ) { product, promotions ->
            val now = Instant.now()
            val activePromotions = promotions.activeAt(now)

            product?.let {
                val finalPromotion = getPromotionForProduct(product, activePromotions)
                ProductWithPromotion(product = it, promotion = finalPromotion)
            }
        }
    }
}