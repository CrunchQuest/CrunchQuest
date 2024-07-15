package com.crunchquest.android.repository

import android.util.Log
import com.crunchquest.android.data.model.User
import com.crunchquest.android.data.network.ApiService
import com.crunchquest.android.data.network.response.ServiceRequestResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.HttpException

class HomeRepository(private val apiService: ApiService) {

    suspend fun getServiceRequests(userId: String): Result<List<ServiceRequestResponse>> {
        return try {
            val response = apiService.getServiceRequests(userId)
            Result.success(response)
        } catch (e: HttpException) {
            // Handle HTTP exceptions (e.g., 4xx, 5xx responses)
            Log.e("HomeRepository", "HTTP error: ${e.code()} - ${e.message()}")
            Result.failure(e)
        } catch (e: Exception) {
            // Handle other exceptions (e.g., network errors)
            Log.e("HomeRepository", "Unexpected error: ${e.message}")
            Result.failure(e)
        }
    }

    fun fetchUserProfileImage(userId: String, callback: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                callback(user?.profileImageUrl ?: "")
            }

            override fun onCancelled(error: DatabaseError) {
                callback("")
            }
        })
    }
}