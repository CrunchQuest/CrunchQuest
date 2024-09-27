package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.ReviewMapper
import com.crunchquest.android.data.source.local.ReviewLocalDataSource
import com.crunchquest.android.data.source.remote.ReviewRemoteDataSource
import com.crunchquest.android.domain.entities.Review
import com.crunchquest.android.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val remoteDataSource: ReviewRemoteDataSource,
    private val localDataSource: ReviewLocalDataSource,
    private val reviewMapper: ReviewMapper
) : ReviewRepository {

    override suspend fun createReview(review: com.crunchquest.android.domain.entities.Review): Result<com.crunchquest.android.domain.entities.Review> {
        return try {
            val reviewRemote = reviewMapper.toRemote(review)
            remoteDataSource.createReview(reviewRemote)
            localDataSource.saveReview(reviewMapper.toLocal(review)) // Cache locally
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReview(reviewId: String): Result<com.crunchquest.android.domain.entities.Review> {
        return try {
            val reviewLocal = localDataSource.getReview(reviewId)
            if (reviewLocal != null) {
                return Result.success(reviewMapper.fromLocal(reviewLocal))
            }

            val reviewRemote = remoteDataSource.getReview(reviewId)
            if (reviewRemote != null) {
                val review = reviewMapper.fromRemote(reviewRemote)
                localDataSource.saveReview(reviewMapper.toLocal(review)) // Cache locally
                return Result.success(review)
            } else {
                Result.failure(Exception("Review not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReviewsByReviewer(reviewerId: String): Result<List<com.crunchquest.android.domain.entities.Review>> {
        return try {
            val localReviews = localDataSource.getReviewsByReviewer(reviewerId).map { reviewMapper.fromLocal(it) }
            if (localReviews.isNotEmpty()) {
                return Result.success(localReviews)
            }

            val remoteReviews = remoteDataSource.getReviewsByReviewer(reviewerId)?.map { reviewMapper.fromRemote(it) }
            if (remoteReviews != null) {
                remoteReviews.forEach { review ->
                    localDataSource.saveReview(reviewMapper.toLocal(review)) // Cache locally
                }
                return Result.success(remoteReviews)
            }

            Result.failure(Exception("No reviews found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReviewsForReviewed(reviewedId: String): Result<List<com.crunchquest.android.domain.entities.Review>> {
        return try {
            val localReviews = localDataSource.getReviewsForReviewed(reviewedId).map { reviewMapper.fromLocal(it) }
            if (localReviews.isNotEmpty()) {
                return Result.success(localReviews)
            }

            val remoteReviews = remoteDataSource.getReviewsForReviewed(reviewedId)?.map { reviewMapper.fromRemote(it) }
            if (remoteReviews != null) {
                remoteReviews.forEach { review ->
                    localDataSource.saveReview(reviewMapper.toLocal(review)) // Cache locally
                }
                return Result.success(remoteReviews)
            }

            Result.failure(Exception("No reviews found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReviewsByRequest(requestId: String): Result<List<com.crunchquest.android.domain.entities.Review>> {
        return try {
            val localReviews = localDataSource.getReviewsByRequest(requestId).map { reviewMapper.fromLocal(it) }
            if (localReviews.isNotEmpty()) {
                return Result.success(localReviews)
            }

            val remoteReviews = remoteDataSource.getReviewsByRequest(requestId)?.map { reviewMapper.fromRemote(it) }
            if (remoteReviews != null) {
                remoteReviews.forEach { review ->
                    localDataSource.saveReview(reviewMapper.toLocal(review)) // Cache locally
                }
                return Result.success(remoteReviews)
            }

            Result.failure(Exception("No reviews found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateReview(review: com.crunchquest.android.domain.entities.Review): Result<Unit> {
        return try {
            val reviewRemote = reviewMapper.toRemote(review)
            remoteDataSource.updateReview(reviewRemote)
            localDataSource.updateReview(reviewMapper.toLocal(review))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            val reviewLocal = localDataSource.getReview(reviewId)
            if (reviewLocal != null) {
                remoteDataSource.deleteReview(reviewId)
                localDataSource.deleteReview(reviewLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Review not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
