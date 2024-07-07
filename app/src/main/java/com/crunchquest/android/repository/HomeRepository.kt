package com.crunchquest.android.repository

import com.crunchquest.android.data.model.User
import com.crunchquest.android.data.network.ApiService
import com.crunchquest.android.data.network.response.ServiceRequestResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeRepository(private val apiService: ApiService) {

    suspend fun getServiceRequests(userId: String): List<ServiceRequestResponse> {
        return apiService.getServiceRequests(userId)
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