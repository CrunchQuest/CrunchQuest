package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.PaymentRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PaymentRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createPayment(paymentRemote: PaymentRemote) {
        firestore.collection("payments").document(paymentRemote.paymentId).set(paymentRemote).await()
    }

    suspend fun getPayment(paymentId: String): PaymentRemote? {
        val paymentDocument = firestore.collection("payments").document(paymentId).get().await()
        return if (paymentDocument.exists()) paymentDocument.toObject(PaymentRemote::class.java) else null
    }

    suspend fun getPaymentsByRequest(requestId: String): List<PaymentRemote>? {
        val result = firestore.collection("payments")
            .whereEqualTo("requestId", requestId)
            .get()
            .await()
        return result.toObjects(PaymentRemote::class.java)
    }

    suspend fun updatePayment(paymentRemote: PaymentRemote) {
        firestore.collection("payments").document(paymentRemote.paymentId).set(paymentRemote).await()
    }

    suspend fun deletePayment(paymentId: String) {
        firestore.collection("payments").document(paymentId).delete().await()
    }
}
