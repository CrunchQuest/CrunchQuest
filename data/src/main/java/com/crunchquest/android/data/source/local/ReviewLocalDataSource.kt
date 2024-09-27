package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "reviews")
data class ReviewLocal(
    @PrimaryKey val reviewId: String,
    val requestId: String,
    val reviewerId: String,
    val reviewedId: String,
    val rating: Int,
    val reviewText: String?,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewLocal)

    @Query("SELECT * FROM reviews WHERE reviewId = :reviewId")
    suspend fun getReview(reviewId: String): ReviewLocal?

    @Query("SELECT * FROM reviews WHERE reviewerId = :reviewerId")
    suspend fun getReviewsByReviewer(reviewerId: String): List<ReviewLocal>

    @Query("SELECT * FROM reviews WHERE reviewedId = :reviewedId")
    suspend fun getReviewsForReviewed(reviewedId: String): List<ReviewLocal>

    @Query("SELECT * FROM reviews WHERE requestId = :requestId")  // New query
    suspend fun getReviewsByRequest(requestId: String): List<ReviewLocal>

    @Update
    suspend fun updateReview(review: ReviewLocal)

    @Delete
    suspend fun deleteReview(review: ReviewLocal)
}

class ReviewLocalDataSource(private val reviewDao: ReviewDao) {

    suspend fun saveReview(reviewLocal: ReviewLocal) {
        reviewDao.insertReview(reviewLocal)
    }

    suspend fun getReview(reviewId: String): ReviewLocal? {
        return reviewDao.getReview(reviewId)
    }

    suspend fun getReviewsByReviewer(reviewerId: String): List<ReviewLocal> {
        return reviewDao.getReviewsByReviewer(reviewerId)
    }

    suspend fun getReviewsForReviewed(reviewedId: String): List<ReviewLocal> {
        return reviewDao.getReviewsForReviewed(reviewedId)
    }

    suspend fun getReviewsByRequest(requestId: String): List<ReviewLocal> {   // New method
        return reviewDao.getReviewsByRequest(requestId)
    }

    suspend fun updateReview(reviewLocal: ReviewLocal) {
        reviewDao.updateReview(reviewLocal)
    }

    suspend fun deleteReview(reviewLocal: ReviewLocal) {
        reviewDao.deleteReview(reviewLocal)
    }
}
