package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "payments")
data class PaymentLocal(
    @PrimaryKey val paymentId: String,
    val requestId: String,
    val payerId: String,
    val payeeId: String,
    val amount: Double,
    val paymentMethod: String,
    val transactionReference: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val releaseDate: String?
)

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentLocal)

    @Query("SELECT * FROM payments WHERE paymentId = :paymentId")
    suspend fun getPayment(paymentId: String): PaymentLocal?

    @Query("SELECT * FROM payments WHERE requestId = :requestId")
    suspend fun getPaymentsByRequest(requestId: String): List<PaymentLocal>

    @Update
    suspend fun updatePayment(payment: PaymentLocal)

    @Delete
    suspend fun deletePayment(payment: PaymentLocal)
}

class PaymentLocalDataSource(private val paymentDao: PaymentDao) {

    suspend fun savePayment(paymentLocal: PaymentLocal) {
        paymentDao.insertPayment(paymentLocal)
    }

    suspend fun getPayment(paymentId: String): PaymentLocal? {
        return paymentDao.getPayment(paymentId)
    }

    suspend fun getPaymentsByRequest(requestId: String): List<PaymentLocal> {
        return paymentDao.getPaymentsByRequest(requestId)
    }

    suspend fun updatePayment(paymentLocal: PaymentLocal) {
        paymentDao.updatePayment(paymentLocal)
    }

    suspend fun deletePayment(paymentLocal: PaymentLocal) {
        paymentDao.deletePayment(paymentLocal)
    }
}
