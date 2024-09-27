package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Review

interface ReviewRepository {
    suspend fun createReview(review: Review): Result<Review>
    suspend fun getReview(reviewId: String): Result<Review>
    suspend fun getReviewsByReviewer(reviewerId: String): Result<List<Review>>
    suspend fun getReviewsForReviewed(reviewedId: String): Result<List<Review>>
    suspend fun getReviewsByRequest(requestId: String): Result<List<Review>>
    suspend fun updateReview(review: Review): Result<Unit>
    suspend fun deleteReview(reviewId: String): Result<Unit>
}

