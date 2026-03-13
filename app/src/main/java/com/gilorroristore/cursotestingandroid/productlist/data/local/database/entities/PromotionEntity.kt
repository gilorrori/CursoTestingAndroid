package com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promotions")
data class PromotionEntity(
    @PrimaryKey
    val id: String,
    val productId: String,
    val type: String,
    val percent: Int? = null,
    val buyX: Int? = null,
    val payY: Int? = null,
    val startAtEpoch: Long? = null,
    val endAtEpoch: Long? = null
)