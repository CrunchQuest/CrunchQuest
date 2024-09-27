package com.crunchquest.android.presentation.ui.general

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.crunchquest.shared.R
import com.crunchquest.android.domain.model.UserPerformance
import com.crunchquest.android.presentation.ui.MainActivity
import com.crunchquest.android.presentation.ui.login.LoginActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val firebaseApp = FirebaseApp.initializeApp(/* context= */ this)!!
        // Register the App Check provider.
        FirebaseAppCheck.getInstance(firebaseApp).installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())

        var imageView = findViewById<ImageView>(R.id.iv_note)

        imageView.alpha = 0f
        imageView.animate().setDuration(5000).alpha(1f).withEndAction {
            val currentUser = FirebaseAuth.getInstance().currentUser
            //no user logged in
            if (currentUser == null) {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
                // THere is a logged in user
            } else {
                // User is logged in and checking if user has done personalization survey
                checkUserPreferencesAndPerformance(currentUser.uid)
            }
        }
    }

    private fun checkUserPreferencesAndPerformance(uid: String) {
        val preferencesRef = FirebaseDatabase.getInstance().getReference("/users/$uid/preferences")
        preferencesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Preferences exist, check user performance
                    val servicesCategories = resources.getStringArray(R.array.category)
                    checkUserPerformance(uid, servicesCategories) {
                        // Navigate to BuyerActivity or any other activity after checking performance
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                } else {
                    // Preferences don't exist, redirect to PreferencesActivity
                    val intent = Intent(this@SplashScreenActivity, PersonalizationActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun checkUserPerformance(uid: String, servicesCategories: Array<String>, onComplete: () -> Unit) {
        val performanceRef = FirebaseDatabase.getInstance().getReference("user_performance/$uid")
        performanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in servicesCategories.indices) {
                    if (!dataSnapshot.hasChild(i.toString())) {
                        val userPerformance = UserPerformance(
                            rating = 0,
                            total = 0,
                            category_name = servicesCategories[i]
                        )
                        performanceRef.child(i.toString()).setValue(userPerformance)
                    }
                }
                onComplete() // Call the onComplete function after checking/initializing performance
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}