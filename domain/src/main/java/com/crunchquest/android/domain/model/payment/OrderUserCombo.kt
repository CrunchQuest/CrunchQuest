package com.crunchquest.android.domain.model.payment

import com.crunchquest.android.domain.model.ServiceRequest
import com.crunchquest.android.domain.entities.User

data class OrderUserCombo(
    val serviceRequest: ServiceRequest,
    val user: User
)