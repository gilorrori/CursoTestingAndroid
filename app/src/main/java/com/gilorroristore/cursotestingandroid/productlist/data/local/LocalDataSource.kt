package com.gilorroristore.cursotestingandroid.productlist.data.local

import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.ProductDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.PromotionDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*Persistencia local en Room, esta clase funciona como puente entre el dao y el repositorio*/
class LocalDataSource @Inject constructor(
    private val productDao: ProductDao,
    private val promotionDao: PromotionDao
) {

    /* metodos que solo funcionan como puente debido a que devuelven lo mismo que los dao */
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun getProductById(id: String): Flow<ProductEntity> {
        return productDao.getProductById(id)
    }

    fun getAllPromotions(): Flow<List<PromotionEntity>> {
        return promotionDao.getAllPromotions()
    }

    suspend fun saveProducts(products: List<ProductEntity>) {
        productDao.replaceAll(products)
    }
}