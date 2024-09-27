package com.crunchquest.android.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.general.ProfileSettingActivity
import com.crunchquest.android.presentation.ui.login.LoginActivity
import com.crunchquest.android.presentation.ui.messages.MessagesActivity
import com.crunchquest.android.presentation.utility.NotificationsWorker
import com.crunchquest.android.presentation.utility.UserSessionManager
import com.crunchquest.presentation.R
import com.crunchquest.presentation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        var currentUser: User? = null
        const val CANCEL_MESSSAGE_NOTIFICATIONS = "cancelmessagesnotifications"
        const val RANDOM_TAG = "Justsomerandometag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        // Initialize User Session Manager and current user
        UserSessionManager.initialize(this)
        currentUser = UserSessionManager.getCurrentUser()

        // Check if user is logged in
        verifyUserIsLoggedIn()

        // Setup navigation with NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        Log.d("MainActivity", "Navigating to HomeFragment")
        setupBottomNavigation()
        handleInternetConnection()
        startBackgroundService()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun handleInternetConnection() {
        if (!isConnected()) {
            binding.networkConnection.visibility = View.VISIBLE
            showNoInternetSnackbar()
        } else {
            binding.networkConnection.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun showNoInternetSnackbar() {
        Snackbar.make(binding.root, "Cannot use the app without internet connection.", Snackbar.LENGTH_INDEFINITE)
            .setAction("SETTINGS") {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
            .show()
    }

    private fun verifyUserIsLoggedIn() {
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun startBackgroundService() {
        val notificationsWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(notificationsWorkRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.crunchquest.shared.R.menu.menu_main, menu)
        val menuItem = menu!!.findItem(com.crunchquest.shared.R.id.addService)
        menuItem.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.crunchquest.shared.R.id.message_iv -> startActivity(Intent(this, MessagesActivity::class.java))
            com.crunchquest.shared.R.id.profileSettings -> {
                val intent = Intent(this, ProfileSettingActivity::class.java)
                intent.putExtra("intent", "buyer")
                startActivity(intent)
            }
            com.crunchquest.shared.R.id.logOut -> showDialogFun()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogFun() {
        AlertDialog.Builder(this)
            .setMessage("Do you want to sign out?")
            .setCancelable(true)
            .setPositiveButton("Proceed") { _, _ ->
                UserSessionManager.clearCurrentUser()  // Clear user session
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                currentUser = null
                finish()
                Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    // Optional: Handle back button navigation with NavController
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}