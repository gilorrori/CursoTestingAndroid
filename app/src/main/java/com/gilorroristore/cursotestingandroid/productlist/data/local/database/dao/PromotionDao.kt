package com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions")
    fun getAllPromotions(): Flow<List<PromotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromotions(promotions: List<PromotionEntity>)

    @Query("DELETE FROM promotions")
    suspend fun clearPromotions()

    /**
     * En una transacción se ejecutan las funciones dentro hasta que ambas se cumplen en este
     * caso hasta que se hayan eliminado los datos e insertado se hará la funcion completamente
     * ejemplo: si se eliminan los datos y al insertar hay un error, se hace rollback y se deja
     * como estaba sin alterar nada
     * O SE CUMPLEN TODAS LAS FUNCIONES DENTRO O VUELVE AL ESTADO ORIGINAL */
    @Transaction
    suspend fun replaceAll(promotions: List<PromotionEntity>) {
        clearPromotions()
        insertPromotions(promotions)
    }
}