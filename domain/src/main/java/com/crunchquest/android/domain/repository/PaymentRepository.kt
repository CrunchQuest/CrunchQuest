package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Payment

interface PaymentRepository {
    suspend fun createPayment(payment: Payment): Result<Payment>
    suspend fun getPayment(paymentId: String): Result<Payment>
    suspend fun getPaymentsByRequest(requestId: String): Result<List<Payment>>
    suspend fun updatePayment(payment: Payment): Result<Unit>
    suspend fun deletePayment(paymentId: String): Result<Unit>
}

