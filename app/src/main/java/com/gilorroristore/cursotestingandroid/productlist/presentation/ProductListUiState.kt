package com.gilorroristore.cursotestingandroid.productlist.presentation

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.models.SortOption

sealed class ProductListUiState {
    data object Loading: ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
    data class Success(
        val products: List<Product>,
        val categories: List<String>?,
        val selectedCategory: String?,
        val sortOption: SortOption?
    ) : ProductListUiState()
}