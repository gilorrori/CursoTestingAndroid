package com.gilorroristore.cursotestingandroid.productlist.domain.usecase

import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {
    operator fun invoke(): Flow<List<ProductWithPromotion>> {

        return combine(
            productRepository.getProducts(),
            promotionRepository.getActivePromotions()
        ) { products, promotions ->

            val now = Instant.now()
            val activePromotions: List<Promotion> = promotions.filter {
                it.startTime <= now && it.endTime >= now
            }

            val productsCombine: List<ProductWithPromotion> = products.map { product ->
                val promotion: ProductPromotion? = getPromotionForProduct(product, activePromotions)
                ProductWithPromotion(product = product, promotion = promotion)
            }

            productsCombine
        }

    }
}