package com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("priceCents") val priceCents: Double,
    @ColumnInfo("category") val category: String?,
    @ColumnInfo("stock") val stock: Int?,
    @ColumnInfo("imageUrl") val imageUrl: String? = null
)
