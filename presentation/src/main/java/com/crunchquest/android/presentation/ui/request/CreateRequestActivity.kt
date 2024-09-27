package com.crunchquest.android.presentation.ui.request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.presentation.utility.UserSessionManager
import com.crunchquest.presentation.R
import com.crunchquest.shared.R.array
import com.crunchquest.presentation.databinding.ActivityCreateRequestBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class CreateRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRequestBinding
    private val viewModel: CreateRequestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSpinners()
        setupDatePicker()
        setupTimePicker()
        setupCreateRequestButton()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupSpinners() {
        // Setup category spinner
        val categories = resources.getStringArray(array.category)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        // Setup payment method spinner
        val paymentMethods = resources.getStringArray(array.paymentMethod)
        val paymentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = paymentAdapter
    }

    private fun setupDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    binding.btnDatePicker.text = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTimePicker() {
        binding.btnTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    binding.btnTimePicker.text = String.format("%02d:%02d", hourOfDay, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupCreateRequestButton() {
        binding.btnCreateRequest.setOnClickListener {
            if (validateFields()) {
                val request = buildRequestObject()
                val requestJson = Gson().toJson(request)
                Log.d("CreateRequestActivity", "Serialized Request JSON: $requestJson")
                viewModel.createRequest(request)
                Log.d("CreateRequestActivity", "Sending request: $request")
            }
        }
    }

    private fun buildRequestObject(): Request {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val categoryId = mapCategoryToId(category)
        val rewards = binding.etRewards.text.toString().toDoubleOrNull() ?: 0.0
        val address = binding.etAddress.text.toString().trim()
        val useCurrentLocation = binding.checkUseCurrentLocation.isChecked
        val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()
        val latitude = if (useCurrentLocation) getCurrentLatitude() else null
        val longitude = if (useCurrentLocation) getCurrentLongitude() else null
        val taskSchedule = getSelectedSchedule()

        return Request(
            requestId = generateRequestId(),
            requesterId = getCurrentUserId(),
            assistantUserId = null,
            title = title,
            description = description,
            categoryId = categoryId,
            status = "Open",
            latitude = latitude,
            longitude = longitude,
            address = address,
            taskSchedule = taskSchedule,
            createdAt = getCurrentTimestamp(),
            updatedAt = getCurrentTimestamp(),
            rewards = rewards,
            notes = null,
            paymentStatus = "Pending",
            paymentMethod = mapPaymentMethodToCode(paymentMethod),
            requesterConfirmed = false,
            assistantConfirmed = false,
            reviewed = false,
            captureResult = null,
            captureResultTimestamp = null
        )
    }

    private fun validateFields(): Boolean {
        var isValid = true

        binding.apply {
            isValid = validateField(etTitle, "Title is required") && isValid
            isValid = validateField(etDescription, "Description is required") && isValid
            isValid = validateRewardsField(etRewards, "Rewards amount is required") && isValid
            if (!checkUseCurrentLocation.isChecked) {
                isValid = validateField(etAddress, "Address is required if not using current location") && isValid
            }
            if (btnDatePicker.text == "Select Date") {
                showToast("Please select a date")
                isValid = false
            }
            if (btnTimePicker.text == "Select Time") {
                showToast("Please select a time")
                isValid = false
            }
            if (spinnerCategory.selectedItem == null) {
                showToast("Please select a category")
                isValid = false
            }
            if (spinnerPaymentMethod.selectedItem == null) {
                showToast("Please select a payment method")
                isValid = false
            }
        }

        return isValid
    }

    private fun validateField(editText: TextInputEditText, errorMessage: String): Boolean {
        return if (editText.text.isNullOrEmpty()) {
            editText.error = errorMessage
            false
        } else {
            editText.error = null
            true
        }
    }

    private fun validateRewardsField(editText: TextInputEditText, errorMessage: String): Boolean {
        return if (editText.text.isNullOrEmpty() || editText.text.toString().toDoubleOrNull() == null) {
            editText.error = errorMessage
            false
        } else {
            editText.error = null
            true
        }
    }

    private fun observeViewModel() {
        viewModel.createRequestResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> {
                    hideLoading()
                    showToast("Request created successfully")
                    finish()
                }
                is Result.Error -> {
                    hideLoading()
                    showToast("Error: ${result.exception.message}")
                }
            }
        }
    }

    private fun getCurrentLatitude(): Double? {
        return 0.0 // Replace with actual location logic
    }

    private fun getCurrentLongitude(): Double? {
        return 0.0 // Replace with actual location logic
    }

    private fun getSelectedSchedule(): String {
        val date = binding.btnDatePicker.text.toString()
        val time = binding.btnTimePicker.text.toString()
        return "$date $time"
    }

    private fun generateRequestId(): String = UUID.randomUUID().toString()

    private fun getCurrentUserId(): String = UserSessionManager.getCurrentUserId() ?: "unknownUserId"

    private fun mapCategoryToId(category: String): String {
        return when (category) {
            "Home Cleaning" -> "cat001"
            "Plumbing" -> "cat002"
            "Electrical" -> "cat003"
            else -> "unknown"
        }
    }

    private fun mapPaymentMethodToCode(paymentMethod: String): String {
        return when (paymentMethod) {
            "QR" -> "qr_code"
            "E-Wallet" -> "e_wallet"
            "Bank Transfer" -> "bank_transfer"
            else -> "unknown"
        }
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}







