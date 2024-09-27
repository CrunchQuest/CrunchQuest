package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.ReviewRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createReview(reviewRemote: ReviewRemote) {
        firestore.collection("reviews").document(reviewRemote.reviewId).set(reviewRemote).await()
    }

    suspend fun getReview(reviewId: String): ReviewRemote? {
        val reviewDocument = firestore.collection("reviews").document(reviewId).get().await()
        return if (reviewDocument.exists()) reviewDocument.toObject(ReviewRemote::class.java) else null
    }

    suspend fun getReviewsByRequest(requestId: String): List<ReviewRemote>? {
        val result = firestore.collection("reviews")
            .whereEqualTo("requestId", requestId)
            .get()
            .await()
        return result.toObjects(ReviewRemote::class.java)
    }

    suspend fun getReviewsByReviewer(reviewerId: String): List<ReviewRemote>? {
        val result = firestore.collection("reviews")
            .whereEqualTo("reviewerId", reviewerId)
            .get()
            .await()
        return result.toObjects(ReviewRemote::class.java)
    }

    suspend fun getReviewsForReviewed(reviewedId: String): List<ReviewRemote>? {
        val result = firestore.collection("reviews")
            .whereEqualTo("reviewedId", reviewedId)
            .get()
            .await()
        return result.toObjects(ReviewRemote::class.java)
    }

    suspend fun updateReview(reviewRemote: ReviewRemote) {
        firestore.collection("reviews").document(reviewRemote.reviewId).set(reviewRemote).await()
    }

    suspend fun deleteReview(reviewId: String) {
        firestore.collection("reviews").document(reviewId).delete().await()
    }
}
