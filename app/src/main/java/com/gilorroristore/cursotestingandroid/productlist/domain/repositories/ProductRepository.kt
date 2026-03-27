package com.gilorroristore.cursotestingandroid.productlist.domain.repositories

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product?>
    /* un set no permite meter dos veces el mismo valor */
    fun getProductsByIds(ids: Set<String>): Flow<List<Product>>
    suspend fun refreshProduct()
}