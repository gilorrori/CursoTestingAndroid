package com.gilorroristore.cursotestingandroid.productlist.data.repositories

import com.gilorroristore.cursotestingandroid.core.domain.coroutines.DispatchersProviders
import com.gilorroristore.cursotestingandroid.productlist.data.local.LocalDataSource
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import com.gilorroristore.cursotestingandroid.productlist.data.mappers.toEntity
import com.gilorroristore.cursotestingandroid.productlist.data.remote.RemoteDataSource
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PromotionRepositoryImpl @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource,
    val dispatchers: DispatchersProviders,
    val json: Json
) : PromotionRepository {
    override fun getActivePromotions(): Flow<List<Promotion>> {
        TODO()
    }

    override suspend fun refreshPromotions() {
        withContext(dispatchers.io) {
            val promotions = remoteDataSource.getPromotions().getOrThrow()
            val promotionsEntity: List<PromotionEntity> = promotions.mapNotNull { it.toEntity(json) }
            localDataSource.savePromotions(promotionsEntity)
        }
    }
}