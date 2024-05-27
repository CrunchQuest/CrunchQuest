package com.example.crunchquest.data.network.response

import com.example.crunchquest.data.model.ServiceRequest

data class ServiceRequestResponse(
    val uid: String,
    val request_data: ServiceRequest,
    val similarity_score: Int,
    val distance: Double
)

data class RequestData(
    val address: String,
    val assistConfirmation: String,
    val bookedBy: String,
    val category: String,
    val categoryId: List<Int>,
    val date: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val modeOfPayment: String,
    val price: Int,
    val time: String,
    val title: String,
    val userUid: String
)
