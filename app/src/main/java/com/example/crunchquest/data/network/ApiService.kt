package com.example.crunchquest.data.network

import com.example.crunchquest.data.network.response.ServiceRequestResponse
import com.example.crunchquest.data.network.response.UserPreferencesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/user/preferences/{user_id}")
    suspend fun getUserPreferences(@Path("user_id") userId: String): UserPreferencesResponse

    @GET("/service_requests")
    suspend fun getServiceRequests(@Query("user_id") userId: String): List<ServiceRequestResponse>
}