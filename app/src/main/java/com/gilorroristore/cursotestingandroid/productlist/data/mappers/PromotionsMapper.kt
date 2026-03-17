package com.gilorroristore.cursotestingandroid.productlist.data.mappers

import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.PromotionEntity
import com.gilorroristore.cursotestingandroid.productlist.data.response.PromotionResponse
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.PromotionType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.Instant

fun PromotionResponse.toEntity(json: Json): PromotionEntity? {

    /* de esta manera nos aseguramos que al guardar simplemente no se consideraran los null*/
    if (startAtEpoch == null || endAtEpoch == null) {
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

fun PromotionEntity.toDomain(json: Json): Promotion? {

    val decodeProductIds = runCatching {
        json.decodeFromString(
            ListSerializer(String.serializer()), productIds
        )
    }.getOrNull()


    // Si el backend no envia un type que haga match con el enum que se tiene, le mandamos uno por defecto con runcatching
    val finalType = runCatching {
        PromotionType.valueOf(
            type.trim().uppercase()
        )
    }.getOrNull()

    if (finalType == null ||decodeProductIds == null) return null

    val finalOfferValue = when(finalType) {
        PromotionType.PERCENT -> percent
        PromotionType.BUY_X_PAY_X -> payY
    }?.toDouble()

    /* Misma validacion si finalPrice es null retorna un promotion null */
    finalOfferValue ?: return null

    return Promotion(
        id = id,
        type = finalType,
        productIds = decodeProductIds,
        value = finalOfferValue,

        startTime = Instant.ofEpochSecond(startAtEpoch),
        endTime = Instant.ofEpochSecond(endAtEpoch),
    )
}