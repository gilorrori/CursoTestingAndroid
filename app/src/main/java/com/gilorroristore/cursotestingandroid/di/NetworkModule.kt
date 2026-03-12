package com.gilorroristore.cursotestingandroid.di

import com.gilorroristore.cursotestingandroid.BuildConfig
import com.gilorroristore.cursotestingandroid.productlist.data.remote.MiniMarketApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    /* se coloca named para identificarlo pq un string los hay en el proyecto entonces debe saber
    * exactamente cual string */
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return "https://raw.githubusercontent.com/gilorrori/minimarket-api/main/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }

        return builder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory(
            "application/json; charset=utf-8".toMediaType()
        )
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            /* Si viene algo en el json y no sabe como tratarlo, lo ignora */
            ignoreUnknownKeys = true
            /* Que el parseo no sea tan exacto, por ejemplo si una cadena no tiene comillas dobles
            * y viene con comillas simples lo acepte*/
            isLenient = true
            /* si la dataclass tiene valores por defecto coerceInputValues se usa para que los ignore*/
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
        @Named("baseUrl") baseUrl: String,
        /* parametro usado apra onvertir el json sede el servicio*/
        converter: Converter.Factory
    ): Retrofit {
        //Como no es un endpoint se debe especificar que nuestro "servicio" es un json
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideMiniMarketApiService(retrofit: Retrofit): MiniMarketApiService {
        return retrofit.create(MiniMarketApiService::class.java)
    }
}