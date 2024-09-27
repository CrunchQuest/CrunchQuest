package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.ReviewRemote
import com.crunchquest.android.data.source.local.ReviewLocal
import com.crunchquest.android.domain.entities.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(reviewRemote: ReviewRemote): Review {
        return Review(
            reviewId = reviewRemote.reviewId,
            requestId = reviewRemote.requestId,
            reviewerId = reviewRemote.reviewerId,
            reviewedId = reviewRemote.reviewedId,
            rating = reviewRemote.rating,
            reviewText = reviewRemote.reviewText,
            createdAt = reviewRemote.createdAt,
            updatedAt = reviewRemote.updatedAt
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(review: Review): ReviewRemote {
        return ReviewRemote(
            reviewId = review.reviewId,
            requestId = review.requestId,
            reviewerId = review.reviewerId,
            reviewedId = review.reviewedId,
            rating = review.rating,
            reviewText = review.reviewText,
            createdAt = review.createdAt,
            updatedAt = review.updatedAt
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(reviewLocal: ReviewLocal): Review {
        return Review(
            reviewId = reviewLocal.reviewId,
            requestId = reviewLocal.requestId,
            reviewerId = reviewLocal.reviewerId,
            reviewedId = reviewLocal.reviewedId,
            rating = reviewLocal.rating,
            reviewText = reviewLocal.reviewText,
            createdAt = reviewLocal.createdAt,
            updatedAt = reviewLocal.updatedAt
        )
    }

    // Convert domain entity to local data model
    fun toLocal(review: Review): ReviewLocal {
        return ReviewLocal(
            reviewId = review.reviewId,
            requestId = review.requestId,
            reviewerId = review.reviewerId,
            reviewedId = review.reviewedId,
            rating = review.rating,
            reviewText = review.reviewText,
            createdAt = review.createdAt,
            updatedAt = review.updatedAt
        )
    }
}
