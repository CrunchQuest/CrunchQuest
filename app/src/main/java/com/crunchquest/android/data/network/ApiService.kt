package com.crunchquest.android.data.network

import com.crunchquest.android.data.model.PriceRequest
import com.crunchquest.android.data.model.PriceResponse
import com.crunchquest.android.data.model.payment.OrderUserCombo
import com.crunchquest.android.data.network.response.PaymentResponse
import com.crunchquest.android.data.network.response.ServiceRequestResponse
import com.crunchquest.android.data.network.response.UserPreferencesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/user/preferences/{user_id}")
    suspend fun getUserPreferences(@Path("user_id") userId: String): UserPreferencesResponse

    @GET("/service_requests")
    suspend fun getServiceRequests(@Query("user_id") userId: String): List<ServiceRequestResponse>

    @POST("/payment/generate_payment_url")
    @Headers("Content-Type: application/json")
    fun getPaymentLink(@Body orderUserCombo: OrderUserCombo): Call<PaymentResponse>

    @POST("price/predict")
    fun getPredictedPrice(@Body request: PriceRequest): Call<PriceResponse>

    @POST("retrain")
    fun postRetrain(): Call<Void>

}