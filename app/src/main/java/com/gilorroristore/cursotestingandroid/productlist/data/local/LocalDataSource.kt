package com.gilorroristore.cursotestingandroid.productlist.data.local

import com.gilorroristore.cursotestingandroid.cart.data.local.database.dao.CartItemDao
import com.gilorroristore.cursotestingandroid.cart.data.local.database.entity.CartItemEntity
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.ProductDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.PromotionDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*Persistencia local en Room, esta clase funciona como puente entre el dao y el repositorio*/
class LocalDataSource @Inject constructor(
    private val productDao: ProductDao,
    private val promotionDao: PromotionDao,
    private val cartItemDao: CartItemDao
) {

    /* P R O D U C T S*/

    /* metodos que solo funcionan como puente debido a que devuelven lo mismo que los dao */
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun getProductById(id: String): Flow<ProductEntity?> {
        return productDao.getProductById(id)
    }

    suspend fun saveProducts(products: List<ProductEntity>) {
        productDao.replaceAll(products)
    }

    /* P R O M O T I O N S */
    fun getAllPromotions(): Flow<List<PromotionEntity>> {
        return promotionDao.getAllPromotions()
    }

    suspend fun savePromotions(promotions: List<PromotionEntity>) {
        promotionDao.replaceAll(promotions)
    }

    /* C A R T */

    fun getAllItemsCart(): Flow<List<CartItemEntity>> = cartItemDao.getAllCartItems()

    suspend fun getCartItemById(productId: String): CartItemEntity? =
        cartItemDao.getCartItemById(productId)


    suspend fun insertCartItem(cartItemEntity: CartItemEntity) {
        cartItemDao.insertCartItem(cartItemEntity)
    }

    /* Retorna un Result para verificar la actualización */
    suspend fun updateCartItem(cartItemEntity: CartItemEntity): Result<Unit> {
        return try {
            cartItemDao.updateCartItem(cartItemEntity)
            Result.success(value = Unit)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun deleteCartItem(cartItemEntity: CartItemEntity): Result<Unit> {
        return try {
            cartItemDao.deleteCartItem(cartItemEntity)
            Result.success(value = Unit)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun clearCart(): Result<Unit> {
        return try {
            cartItemDao.clearCart()
            Result.success(value = Unit)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }
}