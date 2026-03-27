package com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id= :id")
    fun getProductById(id: String): Flow<ProductEntity>

    @Query("SELECT * FROM products WHERE id IN (:productsIds)")
    fun getProductByIds(productsIds: List<String>): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearProducts()

    /**
     * En una transacción se ejecutan las funciones dentro hasta que ambas se cumplen en este
     * caso hasta que se hayan eliminado los datos e insertado se hará la funcion completamente
     * ejemplo: si se eliminan los datos y al insertar hay un error, se hace rollback y se deja
     * como estaba sin alterar nada
     * O SE CUMPLEN TODAS LAS FUNCIONES DENTRO O VUELVE AL ESTADO ORIGINAL */
    @Transaction
    suspend fun replaceAll(products: List<ProductEntity>) {
        clearProducts()
        insertAllProducts(products)
    }
}