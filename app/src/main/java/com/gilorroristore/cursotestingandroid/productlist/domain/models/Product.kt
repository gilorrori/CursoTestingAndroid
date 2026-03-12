package com.gilorroristore.cursotestingandroid.productlist.domain.models

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Double,
    val category: String,
    val stock: Int,
    val imageUrl: String? = null
)