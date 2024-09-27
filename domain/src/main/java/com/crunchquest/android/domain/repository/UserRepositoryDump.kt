package com.crunchquest.android.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crunchquest.android.domain.model.Order
import com.crunchquest.android.domain.entities.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryDump(private val auth: FirebaseAuth, private val database: FirebaseDatabase) {

    fun loginUser(email: String, password: String): LiveData<Boolean> {
        val loginResult = MutableLiveData<Boolean>()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loginResult.value = task.isSuccessful
            }
        return loginResult
    }

    fun checkUserPreferences(uid: String): LiveData<Boolean> {
        val preferencesRef = database.getReference("/users/$uid/preferences")
        val preferencesExist = MutableLiveData<Boolean>()

        preferencesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preferencesExist.value = dataSnapshot.exists()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        return preferencesExist
    }

    fun registerUser(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(callback)
    }

    fun sendEmailVerification(callback: (Task<Void>) -> Unit) {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener(callback)
    }

    fun saveUserToFirebaseDatabase(user: User, callback: (Task<Void>) -> Unit) {
        val ref = database.getReference("/users/${user.userId}")
        ref.setValue(user).addOnCompleteListener(callback)
    }

    fun initializeUserNotifications(uid: String, callback: (Task<Void>) -> Unit) {
        val notificationsRef = database.getReference("notifications/$uid")
        notificationsRef.setValue(0).addOnCompleteListener(callback)
    }

    // Fetch orders booked by a user
    fun fetchOrdersByUser(userId: String, status: String, callback: (List<Order>) -> Unit) {
        val bookedByRef = database.getReference("booked_by/$userId")
        bookedByRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (serviceSnapshot in snapshot.children) {
                    val serviceUid = serviceSnapshot.key ?: continue
                    val serviceOrdersRef = database.getReference("booked_by/$userId/$serviceUid")
                    serviceOrdersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(serviceSnapshot: DataSnapshot) {
                            for (orderSnapshot in serviceSnapshot.children) {
                                val order = orderSnapshot.getValue(Order::class.java)
                                if (order != null && order.status == status) {
                                    orders.add(order)
                                }
                            }
                            callback(orders)
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Fetch orders assigned to a user
    fun fetchOrdersToUser(userId: String, status: String, callback: (List<Order>) -> Unit) {
        val bookedToRef = database.getReference("booked_to/$userId")
        bookedToRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null && order.status == status) {
                        orders.add(order)
                    }
                }
                callback(orders)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}