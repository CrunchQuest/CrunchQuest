package com.example.crunchquest.ui.buyer.buyer_activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.crunchquest.R
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RequestActivity : AppCompatActivity() {


    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var priceEditText: EditText
    lateinit var button: Button
    var serviceRequestHandler = ServiceRequestHandler()
    var currentUserUid = FirebaseAuth.getInstance().uid
    lateinit var counterTextView: TextView
    lateinit var goneTextView: TextView
    lateinit var goneView: View
    lateinit var goneCardView: CardView
    lateinit var toolbar: Toolbar
    lateinit var spinner: Spinner
    lateinit var categoryEditText: EditText

    companion object {
        var isHintGone: Boolean = false
    }

    // Maps
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        // Maps Activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Map everything
        titleEditText = findViewById(R.id.title_activityRequest)
        descriptionEditText = findViewById(R.id.description_activityRequest)
        priceEditText = findViewById(R.id.price_activityRequest)
        counterTextView = findViewById(R.id.counter_activityRequest)
        button = findViewById(R.id.button_requestActivity)
        goneTextView = findViewById(R.id.gotItTextView_activityRequest)
        goneView = findViewById(R.id.goneView_activityRequest)
        goneCardView = findViewById(R.id.howCardView_activityRequest)
        toolbar = findViewById(R.id.toolBar_activityRequest)
        spinner = findViewById(R.id.spinnerCategory_activityRequest)
        categoryEditText = findViewById(R.id.category_activityRequest)
        //Checks the text of the button
        checkFirst()
        button.setOnClickListener {
            if (button.text.toString() == "SUBMIT YOUR REQUEST") {
                checkAndCreate()
            } else if (button.text.toString() == "UPDATE YOUR REQUEST") {
                checkAndUpdate()
            }
        }
        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                val length: Int = descriptionEditText.length()
                val convert = length.toString()
                counterTextView.text = "$convert/300"
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length: Int = descriptionEditText.length()
                val convert = length.toString()
                counterTextView.text = "$convert/300"
            }

            override fun afterTextChanged(s: Editable) {}
        })
        checkHint()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        //spinner
        ArrayAdapter.createFromResource(this, R.array.services_category, android.R.layout.simple_spinner_item)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
        val arrayList = resources.getStringArray(R.array.services_category)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View,
                                        arg2: Int, arg3: Long) {
                categoryEditText.setText(arrayList.get(arg2))
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }


    }

    private fun checkHint() {
        if (isHintGone == true) {
            goneTextView.isGone = true
            goneView.isGone = true
            goneCardView.isGone = true
        }

        goneTextView.setOnClickListener {
            goneTextView.isGone = true
            goneView.isGone = true
            goneCardView.isGone = true
            isHintGone = true

        }
    }

    private fun checkAndCreate() {

        // Request location permission when the user clicks the submit button
        requestLocationPermission()


        //Check the title
        if (titleEditText.text.toString().isEmpty()) {
            titleEditText.error = "Fill up the title"
            titleEditText.requestFocus()
            return
        }
        if (titleEditText.text.toString().length < 11 || titleEditText.text.toString().length > 25 ) {
            titleEditText.error = "The title should be 11-25 characters."
            titleEditText.requestFocus()
            return
        }
        //Check the description
        if (descriptionEditText.text.toString().isEmpty()) {
            descriptionEditText.error = "Add a description"
            descriptionEditText.requestFocus()
            return
        }
        if (descriptionEditText.text.length >= 300) {
            descriptionEditText.error = "Don't exceed 300 characters."
            descriptionEditText.requestFocus()
            return
        }
//        //Check the price
//        var numeric = true
//        try {
//            val num = parseInt(priceEditText.text.toString())
//        } catch (e: NumberFormatException) {
//            numeric = false
//        }
//        if(numeric == false) {
//            priceEditText.error = "Please enter a valid number."
//            priceEditText.requestFocus()
//            return
//        }
        if (priceEditText.text.toString().toInt() <= 10000) {
            priceEditText.error = "Price is below minimum."
            priceEditText.requestFocus()
            return
        }
        if (categoryEditText.text.toString().isEmpty()) {
            spinner.requestFocus()
            Toast.makeText(this, "Choose a category.", Toast.LENGTH_SHORT).show()
            return
        }

        //This may be the problem of the BUG
        // please check this

        //Dialog before sign out
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage("Create request?")
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Create", DialogInterface.OnClickListener { _, _ ->
                    // If everything is good to go.
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            val serviceRequest = ServiceRequest(
                                    userUid = currentUserUid,
                                    description = descriptionEditText.text.toString(),
                                    title = titleEditText.text.toString(),
                                    price = priceEditText.text.toString().toInt(),
                                    category = categoryEditText.text.toString()
                            )
                            if (serviceRequestHandler.createServiceRequest(serviceRequest)) {
                                Toast.makeText(applicationContext, "Request posted.", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Confirmation")
        // show alert dialog
        alert.show()

    }

    private fun checkFirst() {
        if (ManageRequestActivity.serviceRequestGettingEdited == null) {
            button.text = "SUBMIT YOUR REQUEST"
        } else if (ManageRequestActivity.serviceRequestGettingEdited != null) {
            button.text = "UPDATE YOUR REQUEST"
            //Set the values of the views using the global variable serviceGettingEdited
            titleEditText.setText(ManageRequestActivity.serviceRequestGettingEdited!!.title)
            descriptionEditText.setText(ManageRequestActivity.serviceRequestGettingEdited!!.description)
            priceEditText.setText(ManageRequestActivity.serviceRequestGettingEdited!!.price.toString())
            setCategory(ManageRequestActivity.serviceRequestGettingEdited!!.category!!)
        }

    }

    private fun checkAndUpdate() {
        //Check the title
        if (titleEditText.text.toString().isEmpty()) {
            titleEditText.error = "Fill up the title"
            titleEditText.requestFocus()
            return
        }
        if (titleEditText.text.toString().length < 11 || titleEditText.text.toString().length > 25 ) {
            titleEditText.error = "The title should be 11-25 characters."
            titleEditText.requestFocus()
            return
        }
        //Check the description
        if (descriptionEditText.text.toString().isEmpty()) {
            descriptionEditText.error = "Add a description"
            descriptionEditText.requestFocus()
            return
        }
        if (descriptionEditText.text.length >= 300) {
            descriptionEditText.error = "Don't exceed 300 characters."
            descriptionEditText.requestFocus()
            return
        }
        if (priceEditText.text.toString().toInt() <= 10000) {
            priceEditText.error = "Price is below minimum."
            priceEditText.requestFocus()
            return
        }
        if (categoryEditText.text.toString().isEmpty()) {
            spinner.requestFocus()
            Toast.makeText(this, "Choose a category.", Toast.LENGTH_SHORT).show()
            return
        }
        val serviceRequest = ServiceRequest(uid = ManageRequestActivity.serviceRequestGettingEdited!!.uid, title = titleEditText.text.toString(), description = descriptionEditText.text.toString(), price = priceEditText.text.toString().toInt(), userUid = currentUserUid, category = categoryEditText.text.toString()
        )
        if (serviceRequestHandler.updateServiceRequest(serviceRequest)) {
            Toast.makeText(this, "Service request updated", Toast.LENGTH_SHORT).show()
        }
        ManageRequestActivity.serviceRequestGettingEdited = null
        finish()
    }

    private fun setCategory(category: String) {
        var arrayList = resources.getStringArray(R.array.services_category)
        if (category == arrayList[0]) {
            spinner.setSelection(0)
            return
        }
        if (category == arrayList[1]) {
            spinner.setSelection(1)
            return
        }
        if (category == arrayList[2]) {
            spinner.setSelection(2)
            return
        }
        if (category == arrayList[3]) {
            spinner.setSelection(3)
            return
        }
        if (category == arrayList[4]) {
            spinner.setSelection(4)
            return
        }
        if (category == arrayList[5]) {
            spinner.setSelection(5)
            return
        }
        if (category == arrayList[6]) {
            spinner.setSelection(6)
            return
        }
        if (category == arrayList[7]) {
            spinner.setSelection(7)
            return
        }
        if (category == arrayList[8]) {
            spinner.setSelection(8)
            return
        }

    }

    override fun onStop() {
        super.onStop()
        ManageRequestActivity.serviceRequestGettingEdited = null
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Use location.latitude and location.longitude
                // Pass the location data to DisplaySpecificRequestActivity
                goToDisplaySpecificRequestActivity(location)
            }
        }
    }

    private fun goToDisplaySpecificRequestActivity(location: Location) {
        val intent = Intent(this, DisplaySpecificRequestActivity::class.java)
        intent.putExtra("latitude", location.latitude)
        intent.putExtra("longitude", location.longitude)
        startActivity(intent)
    }


}