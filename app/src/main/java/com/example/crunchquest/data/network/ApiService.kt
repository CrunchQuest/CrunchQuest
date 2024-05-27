package com.example.crunchquest.data.network

import com.example.crunchquest.data.model.payment.OrderUserCombo
import com.example.crunchquest.data.network.response.PaymentResponse
import com.example.crunchquest.data.network.response.ServiceRequestResponse
import com.example.crunchquest.data.network.response.UserPreferencesResponse
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

    @POST("/api/getPaymentLink")
    @Headers("Content-Type: application/json")
    fun getPaymentLink(@Body orderUserCombo: OrderUserCombo): Call<PaymentResponse>
}