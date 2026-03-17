package com.gilorroristore.cursotestingandroid.productlist.domain.models

data class ProductWithPromotion(
    val product: Product,
    val promotion: ProductPromotion? = null
)

sealed interface ProductPromotion{

    data class Percent(
        val percent: Double,
        val discountedPrice: Double
    ): ProductPromotion

    data class BuyXPayY(
        val buy: Int,
        val pay: Int,
        val label: String
    ): ProductPromotion
}