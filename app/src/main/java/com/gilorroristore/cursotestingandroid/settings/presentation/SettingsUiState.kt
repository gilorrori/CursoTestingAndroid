package com.gilorroristore.cursotestingandroid.settings.presentation

import com.gilorroristore.cursotestingandroid.core.domain.model.ThemeMode

data class SettingsUiState(
    val inStockOnly: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)