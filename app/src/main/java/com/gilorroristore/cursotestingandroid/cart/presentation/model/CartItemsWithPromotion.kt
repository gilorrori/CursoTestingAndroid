package com.gilorroristore.cursotestingandroid.cart.presentation.model

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion

data class CartItemWithPromotion(
    val cartItem: CartItem,
    val item: ProductWithPromotion
)