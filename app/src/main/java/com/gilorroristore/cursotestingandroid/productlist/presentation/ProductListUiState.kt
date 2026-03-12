package com.gilorroristore.cursotestingandroid.productlist.presentation

sealed class ProductListUiState {
    data object Loading: ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
    data class Success(
        val selectedCategory: String,
        //sortOption
    ) : ProductListUiState()
}