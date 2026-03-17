package com.gilorroristore.cursotestingandroid.productlist.data.repositories

import com.gilorroristore.cursotestingandroid.core.domain.coroutines.DispatchersProviders
import com.gilorroristore.cursotestingandroid.productlist.data.local.LocalDataSource
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import com.gilorroristore.cursotestingandroid.productlist.data.mappers.toDomain
import com.gilorroristore.cursotestingandroid.productlist.data.mappers.toEntity
import com.gilorroristore.cursotestingandroid.productlist.data.remote.RemoteDataSource
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PromotionRepositoryImpl @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource,
    val dispatchers: DispatchersProviders,
    val json: Json
) : PromotionRepository {

    private val refreshScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val refreshMutex = Mutex()

    override fun getActivePromotions(): Flow<List<Promotion>> {
        return localDataSource.getAllPromotions()
            .map { promotionEntities -> promotionEntities.mapNotNull { it.toDomain(json) } }

            .onStart {
                refreshScope.launch {
                    if (!refreshMutex.tryLock()) return@launch

                    try {
                        refreshPromotions()
                    } catch (e: Exception) {

                    } finally {
                        refreshMutex.unlock()
                    }
                }
            }
            .catch {
                // Error desde la funcion getallProducts
            }
    }

    override suspend fun refreshPromotions() {
        withContext(dispatchers.io) {
            val promotions = remoteDataSource.getPromotions().getOrThrow()
            val promotionsEntity: List<PromotionEntity> =
                promotions.mapNotNull { it.toEntity(json) }
            localDataSource.savePromotions(promotionsEntity)
        }
    }
}