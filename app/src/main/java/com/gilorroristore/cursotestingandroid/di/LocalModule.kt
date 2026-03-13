package com.gilorroristore.cursotestingandroid.di

import android.content.Context
import androidx.room.Room
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.MiniMarketDatabase
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.ProductDao
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.dao.PromotionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    const val ROOM_DATABASE_NAME = "mini_market_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): MiniMarketDatabase =
        Room.databaseBuilder(context, MiniMarketDatabase::class.java, ROOM_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideProductDao(db: MiniMarketDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun providePromotionDao(db: MiniMarketDatabase): PromotionDao{
        return db.promotionDao()
    }
}