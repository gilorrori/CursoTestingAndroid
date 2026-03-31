package com.gilorroristore.cursotestingandroid.productlist.domain.usecase

import com.gilorroristore.cursotestingandroid.cart.domain.ex.activeAt
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<ProductWithPromotion>> {

        return combine(
            productRepository.getProducts(),
            promotionRepository.getActivePromotions(),
            settingsRepository.inStockOnly
        ) { products, promotions, inStockOnly ->

            val now = Instant.now()
            val activePromotions: List<Promotion> = promotions.activeAt(now)

            val filteredProduct = if (inStockOnly) {
                products.filter { it.stock > 0 }
            } else {
                products
            }

            val productsCombine: List<ProductWithPromotion> = filteredProduct.map { product ->
                val promotion: ProductPromotion? = getPromotionForProduct(product, activePromotions)
                ProductWithPromotion(product = product, promotion = promotion)
            }

            productsCombine
        }
    }
}