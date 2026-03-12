package com.gilorroristore.cursotestingandroid.productlist.domain.repositories

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product?>
    suspend fun refreshProduct()
}