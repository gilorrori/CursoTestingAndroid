package com.gilorroristore.cursotestingandroid.productlist.domain.usecase

import com.gilorroristore.cursotestingandroid.core.presentation.ex.roundTo2Decimals
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.PromotionType
import javax.inject.Inject

class GetPromotionForProduct @Inject constructor() {

    operator fun invoke(product: Product, promotions: List<Promotion>): ProductPromotion? {
        val productPromos = promotions.filter { it.productIds.contains(product.id) }
        val percentPromos = productPromos.filter { it.type == PromotionType.PERCENT }
            .maxByOrNull { it.value }

        if (percentPromos != null) {
            /* tomando el valor mayor y comprobar que al menos sea 0.0 para evitar negativos y menor al 100%*/
            val percent = percentPromos.value.coerceIn(minimumValue = 0.0, maximumValue = 100.0)
            val discountedPrice = (product.price * (1 - percent / 100.0)).roundTo2Decimals()

            return ProductPromotion.Percent(percent = percent, discountedPrice = discountedPrice)
        }

        val buyPayPromo = productPromos.firstOrNull { it.type == PromotionType.BUY_X_PAY_X }
        if (buyPayPromo != null) {
            val buy = buyPayPromo.buyQuantity ?: return null
            val pay = buyPayPromo.value.toInt().coerceIn(0, buy)

            return ProductPromotion.BuyXPayY(
                buy = buy,
                pay = pay,
                label = "{$buy}x${pay}"
            )
        }
        return null
    }
}