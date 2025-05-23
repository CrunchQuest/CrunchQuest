package com.crunchquest.android.ui.buyer

import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.crunchquest.android.R
import com.crunchquest.android.background.NotificationsWorker
import com.crunchquest.android.data.model.User
import com.crunchquest.android.ui.buyer.buyer_activities.BottomFragmentCreateOrder
import com.crunchquest.android.ui.buyer.buyer_fragments.BuyerManageFragment
import com.crunchquest.android.ui.buyer.buyer_fragments.HomeFragment
import com.crunchquest.android.ui.buyer.buyer_fragments.ProfileFragment
import com.crunchquest.android.ui.general.LoginActivity
import com.crunchquest.android.ui.general.ProfileSettingsActivity
import com.crunchquest.android.ui.messages.MessagesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


lateinit var bottomNavigationBuyer: BottomNavigationView

class BuyerActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val buyerManageFragment = BuyerManageFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var menuItem: MenuItem


    companion object {
        var currentUser: User? = null
        const val CANCEL_MESSSAGE_NOTIFICATIONS = "cancelmessagesnotifications"
        const val RANDOM_TAG = "Justsomerandometag"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buyer)


        verifyUserIsLoggedIn()

        bottomNavigationBuyer = findViewById(R.id.bottomNavigation)
        setSupportActionBar(findViewById(R.id.toolBar))
        val wrapper = findViewById<FrameLayout>(R.id.wrapper)
        val showOrHideThisView = findViewById<ConstraintLayout>(R.id.networkConnection)

        if (isConnected()) {
            wrapper.isGone = false
            supportActionBar!!.show()
            bottomNavigationBuyer.isGone = false
            showOrHideThisView.isGone = true
        } else {
            wrapper.isGone = true
            supportActionBar!!.hide()
            bottomNavigationBuyer.isGone = true
            showOrHideThisView.isGone = false
            val snackbar = Snackbar.make(wrapper, "Cannot use the app without internet connection.", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("SETTINGS") {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
            snackbar.show()
        }


        //Initialize home fragment
        makeCurrentFragment(HomeFragment())
        //Map the bottom navigation view
        bottomNavigationBuyer.menu.findItem(R.id.homePage).isChecked = true
        //Onclick listener for the bottom nav, bottom navigation menu have a corresponding id.
        bottomNavigationBuyer.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> {
                    makeCurrentFragment(homeFragment)
                }
                R.id.notifications -> {
                    makeCurrentFragment(buyerManageFragment)
                }
                R.id.profilePage -> {
                    makeCurrentFragment(profileFragment)
                }
            }
            true
        }
        //image button
        val imageButton = findViewById<ImageButton>(R.id.imageButton_activityBuyer)
        imageButton.setOnClickListener {
            if (isConnected()) {
                wrapper.isGone = false
                supportActionBar!!.show()
                bottomNavigationBuyer.isGone = false
                showOrHideThisView.isGone = true
            }
        }

        try {
            val intentString = intent.getStringExtra(BottomFragmentCreateOrder.TAG)!!
            makeCurrentFragment(buyerManageFragment)
        } catch (e: Exception) {

        }

        backgroundService()


    }

    private fun backgroundService() {
        val notificationsWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                .build()
        WorkManager
                .getInstance(applicationContext)
                .enqueue(notificationsWorkRequest)

    }



    //Options Menu on the upper right side
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        menuItem = menu!!.findItem(R.id.addService)
        menuItem.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.message -> {
                val intent = Intent(this, MessagesActivity::class.java)
                startActivity(intent)

            }
            R.id.profileSettings -> {
                val intent = Intent(this, ProfileSettingsActivity::class.java)
                intent.putExtra("intent", "buyer")
                startActivity(intent)
            }
            R.id.logOut -> {
                showDialogFun()
            }

        }
        return super.onOptionsItemSelected(item)
    }



    private fun showDialogFun() {
        //Dialog before sign out
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to sign out?")
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener { _, _ ->
                    var auth = FirebaseAuth.getInstance()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    currentUser = null
                    finish()
                    Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show()
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Sign Out")
        // show alert dialog
        alert.show()
    }

    //Function that will change the current fragment.
    private fun makeCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.wrapper, fragment)
                commit()
            }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    private fun verifyUserIsLoggedIn() {
        val user = FirebaseAuth.getInstance().currentUser!!
        if (user == null || !user.isEmailVerified) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                    applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }
}