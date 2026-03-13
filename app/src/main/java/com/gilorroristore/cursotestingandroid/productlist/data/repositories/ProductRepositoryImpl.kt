package com.gilorroristore.cursotestingandroid.productlist.data.repositories

import com.gilorroristore.cursotestingandroid.productlist.data.remote.RemoteDataSource
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(val remoteDataSource: RemoteDataSource) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        TODO()
    }

    override fun getProductById(id: String): Flow<Product?> {
        TODO()
    }

    // Obtiene datos de internet por eso es un suspend fun
    override suspend fun refreshProduct() {
        remoteDataSource.getProducts()
    }
}