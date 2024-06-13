package com.crunchquest.android.data.network.response

import com.crunchquest.android.data.model.ServiceRequest

data class ServiceRequestResponse(
    val uid: String,
    val request_data: ServiceRequest,
    val similarity_score: Int,
    val distance: Double
)
