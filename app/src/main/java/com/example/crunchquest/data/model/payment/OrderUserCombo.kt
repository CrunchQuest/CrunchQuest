package com.example.crunchquest.data.model.payment

import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User

data class OrderUserCombo(
    val serviceRequest: ServiceRequest,
    val user: User
)