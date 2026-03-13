package com.gilorroristore.cursotestingandroid.productlist.data.remote

import com.gilorroristore.cursotestingandroid.core.domain.model.AppError
import com.gilorroristore.cursotestingandroid.productlist.data.response.ProductResponse
import com.gilorroristore.cursotestingandroid.productlist.data.response.PromotionResponse
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val miniMarketApiService: MiniMarketApiService) {

    suspend fun getProducts(): Result<List<ProductResponse>> {
        return try {
            val response = miniMarketApiService.getProducts()
            Result.success(response.products)
        } catch (e: Exception) {
            Result.failure(mapToDomainError(e))
        }
    }

    suspend fun getPromotions(): Result<List<PromotionResponse>> {
        return try {
            val response = miniMarketApiService.getPromotions()
            Result.success(response.promotions)
        } catch (e: Exception) {
            Result.failure(mapToDomainError(e))
        }
    }
}


private fun mapToDomainError(e: Exception): AppError {
    return when (e) {
        // Respuesta desconocida
        is UnknownHostException -> AppError.NetworkError
        // Cuando el servidor no responde
        is SocketTimeoutException -> AppError.NetworkError
        // Cuando no hay conexión a internet o la red
        is IOException -> AppError.NetworkError
        is HttpException -> {
            when (e.code()) {
                404 -> AppError.NotFoundError
                else -> AppError.UnknownError(message = e.message ?: "Unknown error")
            }
        }
        else -> AppError.UnknownError(message = e.message ?: "Unknown error")
    }
}