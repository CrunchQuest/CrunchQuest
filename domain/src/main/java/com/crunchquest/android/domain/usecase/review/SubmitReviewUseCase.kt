package com.crunchquest.android.domain.usecase.review

import com.crunchquest.android.domain.entities.Review
import com.crunchquest.android.domain.repository.ReviewRepository
import javax.inject.Inject

class SubmitReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(review: Review): Result<Review> {
        return reviewRepository.createReview(review)
    }
}
