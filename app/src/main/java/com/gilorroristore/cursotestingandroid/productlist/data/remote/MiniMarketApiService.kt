package com.gilorroristore.cursotestingandroid.productlist.data.remote

import com.gilorroristore.cursotestingandroid.productlist.data.response.ProductsResponse
import com.gilorroristore.cursotestingandroid.productlist.data.response.PromotionsResponse
import retrofit2.http.GET

interface MiniMarketApiService {

    @GET("data/products.json")
    suspend fun getProducts(): ProductsResponse

    @GET("data/promotions.json")
    suspend fun getPromotions(): PromotionsResponse

}