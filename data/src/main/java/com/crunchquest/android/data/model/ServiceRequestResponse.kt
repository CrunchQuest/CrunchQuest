package com.crunchquest.android.data.model

import com.crunchquest.android.domain.model.ServiceRequest

data class ServiceRequestResponse(
    val uid: String,
    val request_data: com.crunchquest.android.domain.model.ServiceRequest,
    val similarity_score: Int,
    val distance: Double
)
