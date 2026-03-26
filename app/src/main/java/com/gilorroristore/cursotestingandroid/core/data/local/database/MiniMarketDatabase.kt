package com.gilorroristore.cursotestingandroid.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gilorroristore.cursotestingandroid.cart.data.local.database.dao.CartItemDao
import com.gilorroristore.cursotestingandroid.cart.data.local.database.entity.CartItemEntity
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.ProductDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.PromotionDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity

@Database(
    entities = [ProductEntity::class, PromotionEntity::class, CartItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class MiniMarketDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun promotionDao(): PromotionDao
    abstract fun cartItemDao(): CartItemDao
}