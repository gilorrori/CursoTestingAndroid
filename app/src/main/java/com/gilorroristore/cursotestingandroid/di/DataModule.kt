package com.gilorroristore.cursotestingandroid.di

import com.gilorroristore.cursotestingandroid.core.data.coroutines.DefaultDispatchersProvider
import com.gilorroristore.cursotestingandroid.core.domain.coroutines.DispatchersProviders
import com.gilorroristore.cursotestingandroid.productlist.data.repositories.ProductRepositoryImpl
import com.gilorroristore.cursotestingandroid.productlist.data.repositories.PromotionRepositoryImpl
import com.gilorroristore.cursotestingandroid.productlist.data.repositories.SettingsRepositoryImpl
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.PromotionRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(dispatchersProvider: DefaultDispatchersProvider): DispatchersProviders {
        return dispatchersProvider
    }

    @Provides
    @Singleton
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository {
        return productRepositoryImpl
    }

    @Provides
    @Singleton
    fun providePromotionRepository(promotionRepositoryImpl: PromotionRepositoryImpl): PromotionRepository {
        return promotionRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository {
        return settingsRepositoryImpl
    }
}