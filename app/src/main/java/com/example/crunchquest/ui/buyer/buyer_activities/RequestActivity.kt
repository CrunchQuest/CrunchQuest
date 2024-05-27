package com.example.crunchquest.ui.buyer.buyer_activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.crunchquest.R
import com.example.crunchquest.R.array
import com.example.crunchquest.R.id
import com.example.crunchquest.R.layout
import com.example.crunchquest.data.model.ServiceRequest
import com.example.crunchquest.data.model.User
import com.example.crunchquest.data.model.payment.OrderUserCombo
import com.example.crunchquest.data.network.ApiConfig
import com.example.crunchquest.data.network.ApiService
import com.example.crunchquest.data.network.response.PaymentResponse
import com.example.crunchquest.ui.PaymentActivity
import com.example.crunchquest.utility.handlers.ServiceRequestHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


class RequestActivity : AppCompatActivity() {


    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var priceEditText: EditText
    lateinit var button: Button
    var serviceRequestHandler = ServiceRequestHandler()
    var currentUserUid = FirebaseAuth.getInstance().uid
    lateinit var counterTextView: TextView
    lateinit var toolbar: Toolbar
    lateinit var spinner: Spinner
    lateinit var categoryEditText: EditText

    private lateinit var dateButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var timePicker: TimePicker
    lateinit var spinnerPayment: Spinner
    private lateinit var addressEditText: EditText
    private lateinit var modeEditText: EditText

    private var selectedCategoryIndex: Int = 0

    companion object {
        var isHintGone: Boolean = false
    }

    // Maps
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_request)

        // Maps Activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Map everything
        titleEditText = findViewById(id.title_activityRequest)
        descriptionEditText = findViewById(id.description_activityRequest)
        priceEditText = findViewById(id.price_activityRequest)
        counterTextView = findViewById(id.counter_activityRequest)
        button = findViewById(id.button_requestActivity)
        toolbar = findViewById(id.toolBar_activityRequest)
        spinner = findViewById(id.spinnerCategory_activityRequest)
        categoryEditText = findViewById(id.category_activityRequest)

        addressEditText = findViewById<EditText>(id.etAddress)
        spinnerPayment = findViewById<Spinner>(id.spinnerModeOfPayment_activityRequest)
        modeEditText = findViewById<EditText>(id.etModePayment)
        dateButton = findViewById<Button>(id.btnDate)
        dateTextView = findViewById<TextView>(id.tvDate)
        timePicker = findViewById<TimePicker>(id.timePicker)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        dateButton.setOnClickListener {
            dateTextView.error = null
            dateTextView.text = null

            val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                checkDatePicked(year, month, dayOfMonth)
                
                // Format the picked date
                val pickedDate = "${month(month)} $dayOfMonth, $year"

                // Set the picked date as the text of dateTextView
                dateTextView.text = pickedDate

            }, year, month, day)
            dpd.show()
        }

        modeOfPayment()

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
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, arg2: Int, arg3: Long) {
                categoryEditText.setText(arrayList[arg2])
                selectedCategoryIndex = arg2 // Store the selected index
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }


    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDatePicked(yearPicked: Int, monthPicked: Int, dayOfMonthPicked: Int) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val formatted = current.format(formatter)

        val currentYear = formatted.substring(0, 4).toInt()
        val currentMonth = formatted.substring(4, 6).toInt()
        val currentDay = formatted.substring(6).toInt()

        val monthPickedPlusOne = monthPicked + 1

        Log.d(BottomFragmentAssist.TAG, "CURRENT_DATE : Year: $currentYear Month: $currentMonth Day: $currentDay")
        Log.d(BottomFragmentAssist.TAG, "PICKED_DATE: Year: $yearPicked Month: $monthPickedPlusOne Day: $dayOfMonthPicked")

        if (yearPicked < currentYear) {
            displayToast()
            return
        }
        if ((monthPickedPlusOne < currentMonth) && (yearPicked >= currentYear)) {
            displayToast()
            return
        }
        if ((dayOfMonthPicked < currentDay) && (yearPicked >= currentYear && monthPickedPlusOne == currentMonth)) {
            displayToast()
            return
        }
        var day = ""
        if (dayOfMonthPicked <= 9){
            day = "0$dayOfMonthPicked"
        } else {
            day = currentDay.toString()
        }

        dateTextView.text = "${month(monthPicked)} $day, $yearPicked"


    }

    private fun month(monthOfYear: Int): String? {
        when (monthOfYear) {
            0 -> {
                return "January"
            }
            1 -> {
                return "February"
            }
            2 -> {
                return "March"
            }
            3 -> {
                return "April"
            }
            4 -> {
                return "May"
            }
            5 -> {
                return "June"
            }
            6 -> {
                return "July"
            }
            7 -> {
                return "August"
            }
            8 -> {
                return "September"
            }
            9 -> {
                return "October"
            }
            10 -> {
                return "November"
            }
            else -> {
                return "December"
            }
        }

    }

    private fun displayToast() {
        Toast.makeText(this, "Invalid date. You cannot enter a date from the past.", Toast.LENGTH_LONG).show()
    }

    private fun modeOfPayment() {
        //Add the different service categories to the spinner
        ArrayAdapter.createFromResource(this, R.array.modeOfPayment, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPayment.adapter = adapter
            }
        val arrayList = resources.getStringArray(R.array.modeOfPayment)
        spinnerPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View,
                                        arg2: Int, arg3: Long) {
                modeEditText.setText(arrayList[arg2])
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                modeEditText.text = null

            }
        }
    }

    // Function to check if location is allowed
//    private fun isLocationAllowed(): Boolean {
//        return isLocationAllowed
//    }

    private fun checkAndCreate() {


        //Check the title
        if (titleEditText.text.toString().isEmpty()) {
            titleEditText.error = "Title is required"
            titleEditText.requestFocus()
            return
        }
        if (titleEditText.text.toString().length < 8 || titleEditText.text.toString().length > 50 ) {
            titleEditText.error = "The title should be 8-25 characters."
            titleEditText.requestFocus()
            return
        }
        //Check the description
        if (descriptionEditText.text.toString().isEmpty()) {
            descriptionEditText.error = "Description is required"
            descriptionEditText.requestFocus()
            return
        }
        if (descriptionEditText.text.length >= 300) {
            descriptionEditText.error = "Don't exceed 300 characters."
            descriptionEditText.requestFocus()
            return
        }
//        //Check the price
        val priceText = priceEditText.text.toString()
        if (priceText.isEmpty()) {
            priceEditText.error = "Price is required."
            priceEditText.requestFocus()
            return
        }

        val price = try {
            priceText.toInt()
        } catch (e: NumberFormatException) {
            priceEditText.error = "Please enter a valid number."
            priceEditText.requestFocus()
            return
        }

        if (price <= 10000) {
            priceEditText.error = "Price is below minimum."
            priceEditText.requestFocus()
            return
        }

        //Check the category
        if (categoryEditText.text.toString().isEmpty()) {
            spinner.requestFocus()
            Toast.makeText(this, "Choose a category.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check the date
        if (dateTextView.text.toString().isEmpty()) {
            dateTextView.error = "Date is required."
            dateTextView.requestFocus()
            return
        }

        // Check the time
        if (timePicker.hour == 0 && timePicker.minute == 0) {
            Toast.makeText(this, "Time is required.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check the address
        if (addressEditText.text.toString().isEmpty()) {
            addressEditText.error = "Address is required."
            addressEditText.requestFocus()
            return
        }

        // Check the mode of payment
        if (modeEditText.text.toString().isEmpty()) {
            modeEditText.error = "Mode of payment is required."
            modeEditText.requestFocus()
            return
        }

        //This may be the problem of the BUG
        // please check this

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Create request?")
            .setCancelable(true)
            .setPositiveButton("Create") { _, _ ->
                // Check if the location permission has been granted
                if (ActivityCompat.checkSelfPermission(
                        this@RequestActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@RequestActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)
                {
                    // Location permission not granted or location not allowed
                    createRequest(null) // Pass null location
                } else {
                    // Location permission granted and location allowed
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener(this@RequestActivity) { location ->
                            if (location != null) {
                                // Location available, create the service request with location
                                createRequest(location)
                            } else {
                                // Location not available, create the service request with null location
                                createRequest(null)
                            }
                        }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmation")
        alert.show()
    }

    private fun createRequest(location: Location?) {
        val currentUserUid = FirebaseAuth.getInstance().uid
        val currentUserName = FirebaseAuth.getInstance().currentUser?.displayName
        if (currentUserUid == null) {
            Toast.makeText(applicationContext, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/$currentUserUid")
        val requestUserUid = bookedByRef.push().key!!
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                val categoryIdArray = generateCategoryIdArray(selectedCategoryIndex, 9)
                val serviceRequest = ServiceRequest(
                    uid = requestUserUid,
                    userUid = currentUserUid,
                    description = descriptionEditText.text.toString(),
                    title = titleEditText.text.toString(),
                    price = priceEditText.text.toString().toInt(),
                    category = categoryEditText.text.toString(),
                    categoryId = categoryIdArray,
                    latitude = location?.latitude ?: 0.0,
                    longitude = location?.longitude ?: 0.0,
                    date = dateTextView.text.toString(),
                    time = "${timePicker.hour}:${timePicker.minute}",
                    address = addressEditText.text.toString(),
                    modeOfPayment = modeEditText.text.toString(),
                    name = currentUserName,
                    paymentUrl = "",
                    bookedTo = "",
                    bookedBy = currentUserUid,
                    assistConfirmation = "FALSE"
                )

                // Check if service request was successfully created
                if (serviceRequestHandler.createServiceRequest(serviceRequest)) {
                    // Save the service request to the database
                    val selectedPaymentMode = modeEditText.text.toString()
                    if (selectedPaymentMode.isEmpty()) {
                        modeEditText.error = "Mode of payment is required."
                        modeEditText.requestFocus()
                        return
                    }

                    bookedByRef.child(serviceRequest.uid!!)
                        .child(currentUserUid)
                        .setValue(serviceRequest)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Service request saved successfully, now get the payment link
                                if(selectedPaymentMode == "Mobile Payments") {
                                    val apiService = ApiConfig.retrofitPayment.create(ApiService::class.java)
                                    val orderUserCombo = OrderUserCombo(serviceRequest, user!!)
                                    val call = apiService.getPaymentLink(orderUserCombo)
                                    call.enqueue(object : Callback<PaymentResponse> {
                                        override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                                            if (response.isSuccessful) {
                                                val paymentResponse = response.body()
                                                if (paymentResponse != null) {
                                                    val checkoutUrl = paymentResponse.paymentUrl
                                                    // Open PaymentActivity with the checkout URL
                                                    val intent = Intent(this@RequestActivity, PaymentActivity::class.java)
                                                    intent.putExtra("checkoutUrl", checkoutUrl)
                                                    Log.d("RequestActivity", "Checkout URL: $checkoutUrl")
                                                    startActivity(intent)
                                                    Log.d("RequestActivity", "Starting PaymentActivity with URL: $checkoutUrl")
                                                } else {
                                                    Toast.makeText(applicationContext, "Failed to get payment link.", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                Toast.makeText(applicationContext, "Failed to get payment link.", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                                            Toast.makeText(applicationContext, "Failed to get payment link: ${t.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                } else if (selectedPaymentMode == "Cash" ) {
                                    Toast.makeText(applicationContext, "Service request created successfully.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.d("RequestActivity", "Failed to save service request: ${task.exception?.message}")
                                Toast.makeText(applicationContext, "Failed to post request.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(applicationContext, "Failed to create service request.", Toast.LENGTH_SHORT).show()
                }

                Log.d("RequestActivity", "Service request data: $serviceRequest")
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RequestActivity", "Failed to read user data: ${error.message}")
            }
        })
    }

    fun generateCategoryIdArray(selectedIndex: Int, arraySize: Int): List<Int> {
        val categoryIdArray = MutableList(arraySize) { 0 }
        categoryIdArray[selectedIndex] = 1
        return categoryIdArray
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
        val serviceRequest = ServiceRequest(
            uid = ManageRequestActivity.serviceRequestGettingEdited!!.uid,
            title = titleEditText.text.toString(),
            description = descriptionEditText.text.toString(),
            price = priceEditText.text.toString().toInt(),
            userUid = currentUserUid,
            category = categoryEditText.text.toString(),
            date = dateTextView.text.toString(),
            time = "${timePicker.hour}:${timePicker.minute}",
            address = addressEditText.text.toString(),
            modeOfPayment = modeEditText.text.toString()
        )
        if (serviceRequestHandler.updateServiceRequest(serviceRequest)) {
            Toast.makeText(this, "Service request updated", Toast.LENGTH_SHORT).show()
        }
        ManageRequestActivity.serviceRequestGettingEdited = null
        finish()
    }

    private fun setCategory(category: String) {
        var arrayList = resources.getStringArray(array.services_category)
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



}