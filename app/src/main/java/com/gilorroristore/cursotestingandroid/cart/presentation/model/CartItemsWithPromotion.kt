package com.gilorroristore.cursotestingandroid.cart.presentation.model

import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product

data class CartItemsWithPromotion(
    val cartItem: CartItem,
    val product: Product
)