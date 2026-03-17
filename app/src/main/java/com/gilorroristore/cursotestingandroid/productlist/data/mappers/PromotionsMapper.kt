package com.gilorroristore.cursotestingandroid.productlist.data.mappers

import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import com.gilorroristore.cursotestingandroid.productlist.data.response.PromotionResponse
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

fun PromotionResponse.toEntity(json: Json): PromotionEntity? {

    if (startAtEpoch == null ||endAtEpoch == null){
        return null
    }

    val productIds = listOf(productId)
    val productIdsJson = json.encodeToString(
        serializer = ListSerializer(String.serializer()),
        value = productIds
    )

    return PromotionEntity(
        id = id,
        productIds = productIdsJson,
        type = type,
        percent = percent,
        buyX = buyX,
        payY = payY,
        startAtEpoch = startAtEpoch,
        endAtEpoch = endAtEpoch
    )
}