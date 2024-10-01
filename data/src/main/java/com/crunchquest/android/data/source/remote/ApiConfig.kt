package com.crunchquest.android.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val ML_URL = "https://crunchquest-api-e8fa3c3bab7e.herokuapp.com/"
    private const val PAYMENT_URL = "https://cq-payment-1425524eb51a.herokuapp.com/"
    private const val DB_API_URL = "https://cq-connection-api-1f85877da6db.herokuapp.com/"

    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    val retrofitAi: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ML_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitPayment: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PAYMENT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitDbApi: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DB_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

