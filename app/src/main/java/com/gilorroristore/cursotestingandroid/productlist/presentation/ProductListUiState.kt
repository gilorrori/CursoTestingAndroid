package com.gilorroristore.cursotestingandroid.productlist.presentation

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product

sealed class ProductListUiState {
    data object Loading: ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
    data class Success(
        val products: List<Product>,
        //val categories: List<String>,
        //val selectedCategory: String,
        //sortOption
    ) : ProductListUiState()
}