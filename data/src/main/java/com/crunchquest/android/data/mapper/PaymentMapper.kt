package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.PaymentRemote
import com.crunchquest.android.data.source.local.PaymentLocal
import com.crunchquest.android.domain.entities.Payment
import javax.inject.Inject

class PaymentMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(paymentRemote: PaymentRemote): Payment {
        return Payment(
            paymentId = paymentRemote.paymentId,
            requestId = paymentRemote.requestId,
            payerId = paymentRemote.payerId,
            payeeId = paymentRemote.payeeId,
            amount = paymentRemote.amount,
            paymentMethod = paymentRemote.paymentMethod,
            transactionReference = paymentRemote.transactionReference,
            status = paymentRemote.status,
            createdAt = paymentRemote.createdAt,
            updatedAt = paymentRemote.updatedAt,
            releaseDate = paymentRemote.releaseDate
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(payment: Payment): PaymentRemote {
        return PaymentRemote(
            paymentId = payment.paymentId,
            requestId = payment.requestId,
            payerId = payment.payerId,
            payeeId = payment.payeeId,
            amount = payment.amount,
            paymentMethod = payment.paymentMethod,
            transactionReference = payment.transactionReference,
            status = payment.status,
            createdAt = payment.createdAt,
            updatedAt = payment.updatedAt,
            releaseDate = payment.releaseDate
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(paymentLocal: PaymentLocal): Payment {
        return Payment(
            paymentId = paymentLocal.paymentId,
            requestId = paymentLocal.requestId,
            payerId = paymentLocal.payerId,
            payeeId = paymentLocal.payeeId,
            amount = paymentLocal.amount,
            paymentMethod = paymentLocal.paymentMethod,
            transactionReference = paymentLocal.transactionReference,
            status = paymentLocal.status,
            createdAt = paymentLocal.createdAt,
            updatedAt = paymentLocal.updatedAt,
            releaseDate = paymentLocal.releaseDate
        )
    }

    // Convert domain entity to local data model
    fun toLocal(payment: Payment): PaymentLocal {
        return PaymentLocal(
            paymentId = payment.paymentId,
            requestId = payment.requestId,
            payerId = payment.payerId,
            payeeId = payment.payeeId,
            amount = payment.amount,
            paymentMethod = payment.paymentMethod,
            transactionReference = payment.transactionReference,
            status = payment.status,
            createdAt = payment.createdAt,
            updatedAt = payment.updatedAt,
            releaseDate = payment.releaseDate
        )
    }
}
