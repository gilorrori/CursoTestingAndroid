package com.gilorroristore.cursotestingandroid.detail.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product

@Composable
fun AddToCardButton(
    modifier: Modifier = Modifier,
    product: Product?,
    isLoading: Boolean,
    addToCart: () -> Unit
) {
    product?.let {
        if (it.stock > 0) {
            AddToCardButtonWithStock(
                modifier = modifier,
                product = it,
                isLoading = isLoading,
                addToCart = addToCart
            )
        } else {
            AddToCardButtonNoStock(modifier)
        }
    }
}