package com.crunchquest.android.domain.usecase.payment

import com.crunchquest.android.domain.entities.Payment
import com.crunchquest.android.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: String): Result<Payment> {
        return paymentRepository.getPayment(paymentId)
    }
}
