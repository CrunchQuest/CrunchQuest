package com.crunchquest.android.domain.utility.handlers

import com.crunchquest.android.domain.entities.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserHandler {

    var database: FirebaseDatabase
    var userRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("users")
    }

    fun update(user: User): Boolean {
        userRef.child(user.userId!!).setValue(user)
        return true
    }

}