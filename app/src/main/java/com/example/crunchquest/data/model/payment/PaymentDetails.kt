package com.example.crunchquest.data.model.payment

data class PaymentDetails(
    val payment_method_types: List<String>,
    val payment_due_date: Int,
    val token_id: String,
    val url: String,
    val expired_date: String
)