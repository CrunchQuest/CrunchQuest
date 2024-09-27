package com.crunchquest.android.domain.usecase.payment

import com.crunchquest.android.domain.entities.Payment
import com.crunchquest.android.domain.repository.PaymentRepository
import javax.inject.Inject

class CreatePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(payment: Payment): Result<Payment> {
        return paymentRepository.createPayment(payment)
    }
}
