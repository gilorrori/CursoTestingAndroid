package com.gilorroristore.cursotestingandroid.detail.presentation

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion

data class DetailUiState(
    val item: ProductWithPromotion? = null,
    val isLoading: Boolean = true
)