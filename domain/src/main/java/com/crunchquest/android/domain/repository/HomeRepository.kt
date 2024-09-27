package com.crunchquest.android.domain.repository

import android.util.Log
import com.crunchquest.android.domain.entities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.HttpException

class HomeRepository {

        private val database = FirebaseDatabase.getInstance()
        private val userRef = database.getReference("users")

        fun fetchServiceRequests(onSuccess: (List<User>) -> Unit, onError: (Throwable) -> Unit) {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                    onSuccess(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(Throwable(error.message))
                }
            })
        }

        fun fetchUserProfileImage(userId: String, onSuccess: (String) -> Unit) {
            userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        user.profilePicture?.let { onSuccess(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeRepository", error.message)
                }
            })
        }

        fun getUser(uid: String, onSuccess: (User) -> Unit, onError: (Throwable) -> Unit) {
            userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onError(Throwable("User not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(Throwable(error.message))
                }
            })
        }

        fun updateUser(user: User, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
            userRef.child(user.userId).setValue(user)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it)
                }
        }

        fun deleteUser(uid: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
            userRef.child(uid).removeValue()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it)
                }
        }
}