package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.crunchquest.android.domain.entities.User

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val bio: String?,
    val avgRating: Double?,
    val dateOfBirth: String?,
    val gender: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val idCard: String?,
    val preference: String?,
    val createdAt: String,
    val updatedAt: String,
    val lastLogin: String?,
    val status: String,
    val role: String,
    val verificationStatus: String?,
    val workExperience: String?
)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserLocal)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUser(userId: String): UserLocal?

    @Query("SELECT verificationStatus FROM users WHERE userId = :userId")
    suspend fun isEmailVerified(userId: String): String
}

class UserLocalDataSource(private val userDao: UserDao) {

    // Function to save a user locally
    suspend fun saveUser(userLocal: UserLocal) {
        userDao.insertUser(userLocal)
    }

    // Function to get a user by userId from local storage
    suspend fun getUser(userId: String): UserLocal? {
        return userDao.getUser(userId)
    }

    // Function to check if a user's email is verified
    suspend fun isEmailVerified(userId: String): Boolean {
        return userDao.isEmailVerified(userId) == "TRUE"
    }
}
