package com.crunchquest.android.presentation.utility

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.karn.notify.Notify

class NotificationsWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    // No need to create an instance since UserSessionManager is a singleton object

    override fun doWork(): Result {
        // Do the work here
        showNotification()
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun showNotification() {
        // Directly access the UserSessionManager instance
        val currentUser = UserSessionManager.getCurrentUser()

        if (currentUser == null) {
            Log.e("NotificationsWorker", "Current user is null, cannot fetch notifications.")
            return
        }

        val notificationsRef = FirebaseDatabase.getInstance().getReference("/notifications/")
        notificationsRef.child(currentUser.userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val count = snapshot.value?.toString()?.toIntOrNull() ?: 0
                    if (count > 0) {
                        Notify
                            .with(applicationContext)
                            .content { // this: Payload.Content.Default
                                title = "New Booking"
                                text = "You have a new booking, please check your orders."
                            }
                            .show()

                        // Reset notification count to 0 after showing
                        notificationsRef.child(currentUser.userId).setValue(0)
                    }
                } catch (e: Exception) {
                    Log.e("NotificationsWorker", "Error fetching notifications: ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NotificationsWorker", "Database error: ${error.message}")
            }
        })
    }
}

