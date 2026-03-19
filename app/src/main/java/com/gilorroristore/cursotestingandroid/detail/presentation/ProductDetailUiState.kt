package com.gilorroristore.cursotestingandroid.detail.presentation

import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion

data class ProductDetailUiState(
    val item: ProductWithPromotion? = null,
    val isLoading: Boolean = true
)