package com.gilorroristore.cursotestingandroid.productlist.domain.repositories

import com.gilorroristore.cursotestingandroid.core.domain.model.ThemeMode
import com.gilorroristore.cursotestingandroid.productlist.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val inStockOnly: Flow<Boolean>
    val themeMode:Flow<ThemeMode>
    val selectedCategory:Flow<String?>
    val filtersVisible: Flow<Boolean>
    val sortOption:Flow<SortOption>

    suspend fun setInStockOnly(value:Boolean)
    suspend fun setThemeMode(value:ThemeMode)
    suspend fun setSelectedCategory(value:String?)
    suspend fun setFiltersVisible(value:Boolean)
    suspend fun setSortOption(value:SortOption)
}