package com.gilorroristore.cursotestingandroid.productlist.domain.repositories

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import kotlinx.coroutines.flow.Flow

interface PromotionRepository {
    fun getActivePromotions(): Flow<List<Promotion>>
    suspend fun refreshPromotions()
}