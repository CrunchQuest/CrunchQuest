package com.crunchquest.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crunchquest.android.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository(private val auth: FirebaseAuth, private val database: FirebaseDatabase) {

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
        val ref = database.getReference("/users/${user.uid}")
        ref.setValue(user).addOnCompleteListener(callback)
    }

    fun initializeUserNotifications(uid: String, callback: (Task<Void>) -> Unit) {
        val notificationsRef = database.getReference("notifications/$uid")
        notificationsRef.setValue(0).addOnCompleteListener(callback)
    }

}