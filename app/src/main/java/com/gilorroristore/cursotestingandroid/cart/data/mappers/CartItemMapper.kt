package com.gilorroristore.cursotestingandroid.cart.data.mappers

import com.gilorroristore.cursotestingandroid.cart.data.local.database.entity.CartItemEntity
import com.gilorroristore.cursotestingandroid.cart.domain.model.CartItem

fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        productId = productId,
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        productId = productId,
        quantity = quantity
    )
}