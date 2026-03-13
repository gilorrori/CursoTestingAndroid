package com.gilorroristore.cursotestingandroid.productlist.data.mappers

import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import com.gilorroristore.cursotestingandroid.productlist.data.response.ProductResponse
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product

/**
 * Clase encargada de mappear los modelos entre las diferentes capas
 *
 */
fun ProductResponse.toEntity(): ProductEntity {
    val priceFinal: Double = priceCents?.div(100.0) ?: 0.0

    return ProductEntity(
        id = id,
        name = name,
        description = description,
        priceCents = priceFinal,
        category = category,
        stock = stock,
        imageUrl = imageUrl
    )
}

fun ProductEntity.toDomain(): Product? {
    /*Como la categoria es sumamente imporante aqui, sino existe se devuelve un producto null,
    * por eso puede ser nullable Product? */
    if (category.isNullOrEmpty()) {
        return null
    }

    return Product(
        id = id,
        name = name,
        description = description.orEmpty(),
        priceCents = priceCents,
        category = category,
        stock = stock ?: 0,
        imageUrl = imageUrl
    )
}