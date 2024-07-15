package com.crunchquest.android.data.model.payment

import com.crunchquest.android.data.model.ServiceRequest
import com.crunchquest.android.data.model.User

data class OrderUserCombo(
    val serviceRequest: ServiceRequest,
    val user: User
)