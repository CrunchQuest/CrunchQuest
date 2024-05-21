package com.example.crunchquest.data.network.response

data class ServiceRequestResponse(
    val uid: String,
    val request_data: RequestData,
    val similarity_score: Int
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
