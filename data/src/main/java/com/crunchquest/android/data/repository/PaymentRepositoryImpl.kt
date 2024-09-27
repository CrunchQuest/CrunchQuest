package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.PaymentMapper
import com.crunchquest.android.data.source.local.PaymentLocalDataSource
import com.crunchquest.android.data.source.remote.PaymentRemoteDataSource
import com.crunchquest.android.domain.entities.Payment
import com.crunchquest.android.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val remoteDataSource: PaymentRemoteDataSource,
    private val localDataSource: PaymentLocalDataSource,
    private val paymentMapper: PaymentMapper
) : PaymentRepository {

    override suspend fun createPayment(payment: com.crunchquest.android.domain.entities.Payment): Result<com.crunchquest.android.domain.entities.Payment> {
        return try {
            val paymentRemote = paymentMapper.toRemote(payment)
            remoteDataSource.createPayment(paymentRemote)
            localDataSource.savePayment(paymentMapper.toLocal(payment)) // Cache locally
            Result.success(payment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPayment(paymentId: String): Result<com.crunchquest.android.domain.entities.Payment> {
        return try {
            val paymentLocal = localDataSource.getPayment(paymentId)
            if (paymentLocal != null) {
                return Result.success(paymentMapper.fromLocal(paymentLocal))
            }

            val paymentRemote = remoteDataSource.getPayment(paymentId)
            if (paymentRemote != null) {
                val payment = paymentMapper.fromRemote(paymentRemote)
                localDataSource.savePayment(paymentMapper.toLocal(payment)) // Cache locally
                return Result.success(payment)
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentsByRequest(requestId: String): Result<List<com.crunchquest.android.domain.entities.Payment>> {
        return try {
            val localPayments = localDataSource.getPaymentsByRequest(requestId).map { paymentMapper.fromLocal(it) }
            if (localPayments.isNotEmpty()) {
                return Result.success(localPayments)
            }

            val remotePayments = remoteDataSource.getPaymentsByRequest(requestId)?.map { paymentMapper.fromRemote(it) }
            if (remotePayments != null) {
                remotePayments.forEach { payment ->
                    localDataSource.savePayment(paymentMapper.toLocal(payment)) // Cache locally
                }
                return Result.success(remotePayments)
            }

            Result.failure(Exception("No payments found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePayment(payment: com.crunchquest.android.domain.entities.Payment): Result<Unit> {
        return try {
            val paymentRemote = paymentMapper.toRemote(payment)
            remoteDataSource.updatePayment(paymentRemote)
            localDataSource.updatePayment(paymentMapper.toLocal(payment))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePayment(paymentId: String): Result<Unit> {
        return try {
            val paymentLocal = localDataSource.getPayment(paymentId)
            if (paymentLocal != null) {
                remoteDataSource.deletePayment(paymentId)
                localDataSource.deletePayment(paymentLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
