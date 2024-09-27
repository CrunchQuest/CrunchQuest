package com.crunchquest.android.data.remote.api

import com.crunchquest.android.domain.model.PriceRequest
import com.crunchquest.android.domain.model.PriceResponse
import com.crunchquest.android.data.model.PaymentResponse
import com.crunchquest.android.data.model.ServiceRequestResponse
import com.crunchquest.android.data.model.UserPreferencesResponse
import com.crunchquest.android.data.model.remote.AssistantRemote
import com.crunchquest.android.data.model.remote.BookingRemote
import com.crunchquest.android.data.model.remote.RequestRemote
import com.crunchquest.android.data.model.remote.UserRemote
import com.crunchquest.android.data.remote.dto.LoginRequest
import com.crunchquest.android.data.remote.dto.RegisterRequest
import com.crunchquest.android.data.remote.dto.UpdateUserRequest
import com.crunchquest.android.data.remote.dto.VerificationRequest
import com.crunchquest.android.data.remote.model.LoginResponse
import com.crunchquest.android.data.remote.model.NotificationResponse
import com.crunchquest.android.data.remote.model.RegisterResponse
import com.crunchquest.android.domain.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/user/preferences/{user_id}")
    suspend fun getUserPreferences(@Path("user_id") userId: String): UserPreferencesResponse

    @GET("/service_requests")
    suspend fun getServiceRequests(@Query("user_id") userId: String): List<ServiceRequestResponse>

    @POST("/payment/generate_payment_url")
    @Headers("Content-Type: application/json")
    fun getPaymentLink(@Body orderUserCombo: com.crunchquest.android.domain.model.payment.OrderUserCombo): Call<PaymentResponse>

    @POST("price/predict")
    fun getPredictedPrice(@Body request: PriceRequest): Call<PriceResponse>

    @POST("retrain")
    fun postRetrain(): Call<Void>

    //Divider Of the new Approach of API

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("/users/{id}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserRemote>

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("userId") userId: String, @Body userUpdateRequest: UserRemote): Response<Unit>

    @POST("/auth/send-verification-email")
    suspend fun sendVerificationEmail(): Response<Unit>

    @POST("/api/auth/verify-code")
    suspend fun verifyCode(@Body request: VerificationRequest): Response<Unit>

    @PUT("/user/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: String,
        @Body updateRequest: UpdateUserRequest
    ): Response<RegisterResponse.UserResponse>


    @GET("/notifications/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: String): Response<List<NotificationResponse>>

    // Request Endpoints
    @POST("/api/requests")
    suspend fun createRequest(@Body request: RequestRemote): Response<RequestRemote>

    //Status is a query parameter
    @GET("/api/requests")
    suspend fun getRequests(@Query("status") status: String): Response<List<RequestRemote>> // Changed to query parameter

    @GET("/api/requests/{requestId}")
    suspend fun getRequest(@Path("requestId") requestId: String): Response<RequestRemote>

    @PUT("/api/requests/{requestId}")
    suspend fun updateRequest(@Path("requestId") requestId: String, @Body request: RequestRemote): Response<Unit>

    @DELETE("/api/requests/{requestId}")
    suspend fun deleteRequest(@Path("requestId") requestId: String): Response<Unit>

    // Assist Endpoints
    @POST("/api/assistants")
    suspend fun createAssist(@Body assistant: AssistantRemote): Response<AssistantRemote>

    // Status is a query parameter
    @GET("/api/assistants")
    suspend fun getAssists(@Query("assistanceStatus") assistanceStatus: String): Response<List<AssistantRemote>> // Changed to query parameter

    @GET("/api/assistants/{assistantId}")
    suspend fun getAssist(@Path("assistantId") assistantId: String): Response<AssistantRemote>

    @PUT("/api/assistants/{assistantId}")
    suspend fun updateAssist(@Path("assistantId") assistantId: String, @Body assistant: AssistantRemote): Response<Unit>

    @DELETE("/api/assistants/{assistantId}")
    suspend fun deleteAssist(@Path("assistantId") assistantId: String): Response<Unit>

    // Booking Endpoints
    @POST("/api/bookings")
    suspend fun createBooking(@Body booking: BookingRemote): Response<BookingRemote>

    // Status is a query parameter
    @GET("/api/bookings")
    suspend fun getBookings(@Query("bookingStatus") bookingStatus: String): Response<List<BookingRemote>> // Changed to query parameter

    @GET("/api/bookings/{bookingId}")
    suspend fun getBooking(@Path("bookingId") bookingId: String): Response<BookingRemote>

    @GET("/api/bookings/customer/{customerId}")
    suspend fun getBookingByCustomer(@Path("customerId") customerId: String): Response<List<BookingRemote>>

    @PUT("/api/bookings/{bookingId}")
    suspend fun updateBooking(@Path("bookingId") bookingId: String, @Body booking: BookingRemote): Response<Unit>

    @DELETE("/api/bookings/{bookingId}")
    suspend fun deleteBooking(@Path("bookingId") bookingId: String): Response<Unit>

    @GET("/api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): Response<User>

    @GET("/api/requests/{requestId}/details")
    suspend fun getRequestDetails(@Path("requestId") requestId: String): Response<RequestRemote>

    @POST("/api/requests/{requestId}/cancel")
    suspend fun cancelRequest(@Path("requestId") requestId: String): Response<Unit>

    @GET("/api/requests/feature")
    suspend fun getFeaturedRequests(): Response<List<RequestRemote>>
}