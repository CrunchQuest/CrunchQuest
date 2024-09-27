package com.crunchquest.android.domain.usecase.review

import com.crunchquest.android.domain.entities.Review
import com.crunchquest.android.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewsByUserUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Review>> {
        return reviewRepository.getReviewsByReviewer(userId)
    }
}
