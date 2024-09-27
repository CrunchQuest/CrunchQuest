package com.crunchquest.android.presentation.ui.serviceprovider

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.crunchquest.shared.R
import com.crunchquest.android.domain.model.Order
import com.crunchquest.android.presentation.ui.MainActivity
import com.crunchquest.android.presentation.ui.login.LoginActivity
import com.crunchquest.android.presentation.ui.general.ProfileSettingActivity
import com.crunchquest.android.presentation.ui.messages.MessagesActivity
import com.crunchquest.android.presentation.ui.serviceprovider.seller_activities.CreateServicesActivity
import com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments.SellerManageFragment
import com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments.SellerNotificationsFragment
import com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments.SellerProfileFragment
import com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments.SellerServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

lateinit var bottomNavigationSeller: BottomNavigationView

class SellerActivity : AppCompatActivity() {

    private val sellerManage = SellerManageFragment()
    private val sellerNotifications = SellerNotificationsFragment()
    private val sellerProfile = SellerProfileFragment()
    private val sellerServices = SellerServicesFragment()
    lateinit var menuItem: MenuItem
    var menuToHide: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)


        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar)


        //Action bar at the top
        setSupportActionBar(toolBar)
        //supportActionBar!!.setDisplayShowTitleEnabled(false) //Removes the title

        //Initialize Profile page of the seller fragment
        makeCurrentFragment(SellerServicesFragment())

        //Map the bottom navigation view
        bottomNavigationSeller = findViewById(R.id.bottomNavigation)
        //Onclick listener for the bottom nav, bottom navigation menu have a corresponding id.
        bottomNavigationSeller.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Seller_servicesPage -> {
                    makeCurrentFragment(sellerServices)
                }
                R.id.Seller_manageOrdersPage -> {
                    makeCurrentFragment(sellerManage)
                }
                R.id.Seller_notificationsPage -> {
                    makeCurrentFragment(sellerNotifications)
                }
                R.id.Seller_profilePage -> {
                    makeCurrentFragment(sellerProfile)
                }
            }
            true
        }

        fetchTotalJobsCompleted()

    }

    private fun fetchTotalJobsCompleted() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("booked_to/$currentUserUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalCount: Int = 0
                snapshot.children.forEach { bookings ->
                    val order = bookings.getValue(Order::class.java)!!
                    if (order.status == "COMPLETED") {
                        totalCount += 1
                    }
                }
                val userSellerInfoRef = FirebaseDatabase.getInstance().getReference("user_seller_info/$currentUserUid")
                userSellerInfoRef.child("totalJobsFinished").setValue(totalCount)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
//        menuItem = menu!!.findItem(R.id.search)
        if (menu != null) {
            menuToHide = menu.findItem(R.id.addService)
        }
        menuItem.isVisible = false
        menuToHide?.isVisible = false


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.message -> {
                val intent = Intent(this, MessagesActivity::class.java)
                startActivity(intent)

            }
            R.id.addService -> {
                val intent = Intent(this, CreateServicesActivity::class.java)
                startActivity(intent)
            }
            R.id.profileSettings -> {
                val intent = Intent(this, ProfileSettingActivity::class.java)
                intent.putExtra("intent", "seller")
                startActivity(intent)

            }
            R.id.logOut -> {
                //Dialog before sign out
                val dialogBuilder = AlertDialog.Builder(this)
                // set message of alert dialog
                dialogBuilder.setMessage("Do you want to sign out?")
                        // if the dialog is cancelable
                        .setCancelable(true)
                        // positive button text and action
                        .setPositiveButton("Proceed") { _, _ ->
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            MainActivity.currentUser = null
                            finish()
                            Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show()
                        }
                        // negative button text and action
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.cancel()
                        }
                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Sign Out")
                // show alert dialog
                alert.show()


            }
//            R.id.changeMode -> {
//                startActivity(Intent(this, ChooseActivity::class.java))
//            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun makeCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.wrapper, fragment)
                commit()
            }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onResume() {
        super.onResume()
        menuToHide?.let {
            // Set title bar
            setActionBarTitle("Profile Page")
            menuItem = bottomNavigationSeller.menu.findItem(R.id.Seller_profilePage)
            menuItem.isChecked = true
        } ?: run {
            // Handle the case where menuToHide is not initialized
            Log.e("SellerActivity", "menuToHide is not initialized")
        }
    }

}