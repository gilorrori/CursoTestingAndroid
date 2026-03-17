@file:OptIn(ExperimentalTime::class)

package com.gilorroristore.cursotestingandroid.productlist.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

enum class PromotionType {
    PERCENT,
    BUY_X_PAY_X
}

data class Promotion(
    val id: String,
    val type: PromotionType,
    val productIds: List<String>,
    val value: Double,
    val buyQuantity: Int? = null,
    val startTime: Instant,
    val endTime: Instant
)