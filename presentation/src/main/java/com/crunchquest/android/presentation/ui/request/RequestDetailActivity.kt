package com.crunchquest.android.presentation.ui.request

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.presentation.ui.adapter.AssistantAdapter
import com.crunchquest.presentation.R
import com.crunchquest.presentation.databinding.ActivityRequestDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RequestDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestDetailBinding
    private val viewModel: RequestDetailViewModel by viewModels()

    private var requestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupObservers()

        // Get request data passed via intent
        requestId = intent.getStringExtra("REQUEST_ID")

        if (requestId != null) {
            viewModel.fetchRequestDetail(requestId!!)
        } else {
            showError("Request ID is missing.", false)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Disable default title display

        // Set up back button to navigate back to the previous screen
        binding.btnBack.setOnClickListener {
            finish() // This will navigate back to ActivityFragment
        }

        // Set up the title, share, and report buttons
        binding.toolbarTitle.text = "Request Detail"
        binding.btnShare.setOnClickListener { shareRequest() }
        binding.btnReport.setOnClickListener { reportRequest() }
    }

    private fun reportRequest() {
        Toast.makeText(this, "Report request", Toast.LENGTH_SHORT).show()
    }

    private fun shareRequest() {
        Toast.makeText(this, "Share request", Toast.LENGTH_SHORT).show()
    }

    private fun setupObservers() {
        viewModel.requestDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> {
                    hideLoading()
                    setupRequestInfo(result.data)
                    setupButtons(result.data)
                }
                is Result.Error -> showError(result.exception.message ?: "An error occurred.", false)
            }
        }

        viewModel.assistantList.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> setupAssistRecyclerView(result.data)
                is Result.Error -> {
                    hideLoading()
                    showError(result.exception.message ?: "No assistant list found", false)
                    setupAssistRecyclerView(null) // Show empty state in RecyclerView
                }
            }
        }
    }

    private fun setupRequestInfo(request: Request) {
        binding.textRequestTitle.text = request.title
        binding.textRequestStatus.text = request.status
        binding.textDescription.text = request.description
        binding.textCategory.text = request.categoryId
        binding.textRewards.text = request.rewards.toString()
        binding.textPaymentStatus.text = request.paymentStatus
        binding.textDateTime.text = request.taskSchedule

        // Update status indicator and RecyclerView based on the request status
        updateStatusIndicator(request.status)
        if (request.status == "Pending") {
            viewModel.fetchAssistantList(request.requestId)
        } else {
            setupAssistRecyclerView(null)
        }
    }

    private fun updateStatusIndicator(status: String) {
        when (status) {
            "Open" -> {
                binding.statusIndicator.setBackgroundResource(com.crunchquest.shared.R.drawable.ic_filter)
                binding.textRequestStatus.text = "Open"
            }
            "Pending" -> {
                binding.statusIndicator.setBackgroundResource(com.crunchquest.shared.R.drawable.ic_black_search)
                binding.textRequestStatus.text = "Pending"
            }
            else -> {
                binding.statusIndicator.setBackgroundResource(com.crunchquest.shared.R.drawable.ic_cancel)
                binding.textRequestStatus.text = "Unknown"
            }
        }
    }

    private fun setupAssistRecyclerView(assistantList: AssistantList?) {
        // Initialize the adapter with empty data first
        val adapter = AssistantAdapter(
            getUserById = { userId -> viewModel.getUserById(userId) },
            onAssistantClick = { assistant -> onAssistantSelected(assistant) }
        )

        // Set up RecyclerView before any data is submitted
        binding.recyclerViewAssistance.apply {
            layoutManager = LinearLayoutManager(this@RequestDetailActivity)
            this.adapter = adapter
        }

        if (assistantList == null || assistantList.arrayAssistantUserId.isEmpty()) {
            // Display a message if no assistants are assisting yet
            binding.recyclerViewAssistance.visibility = View.GONE
            binding.emptyStateTextView.apply {
                text = "No users are assisting yet."
                visibility = View.VISIBLE
            }
        } else {
            binding.recyclerViewAssistance.visibility = View.VISIBLE
            binding.emptyStateTextView.visibility = View.GONE

            // Fetch and display assistant data
            CoroutineScope(Dispatchers.Main).launch {
                val assistants = assistantList.arrayAssistantUserId.mapNotNull { userId ->
                    val result = withContext(Dispatchers.IO) { viewModel.getUserById(userId) }
                    when (result) {
                        is Result.Success -> {
                            Assistant(
                                assistantId = result.data.userId, // Use a unique ID
                                requestId = assistantList.requestId,
                                assistantUserId = userId,
                                assistanceStatus = assistantList.assistanceStatus,
                                proposedRewards = null,
                                notes = null,
                                taskAvailability = "Anytime",
                                assistConfirmed = false,
                                paymentRequested = false,
                                startedExecuting = false,
                                createdAt = assistantList.createdAt,
                                updatedAt = assistantList.updatedAt
                            )
                        }
                        is Result.Error -> {
                            Log.e("RequestDetailActivity", "Error fetching user data for $userId: ${result.exception.message}")
                            null
                        }
                        Result.Loading -> null
                    }
                }
                // Submit the fetched list to the adapter
                adapter.submitList(assistants)
            }
        }
    }

    private fun setupButtons(request: Request) {
        binding.btnConfirmAssistance.apply {
            when (request.status) {
                "Open" -> {
                    text = "Waiting Assistance"
                    isEnabled = false
                }
                "Pending" -> {
                    text = "Confirm Assistance"
                    isEnabled = true
                    setOnClickListener { viewModel.confirmAssistance(request) }
                }
                else -> {
                    visibility = View.GONE
                }
            }
        }

        binding.btnCancel.setOnClickListener { cancelRequest() }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String, finishActivity: Boolean = false) {
        Log.e("RequestDetailActivity", message)
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
        if (finishActivity) finish()
    }

    private fun cancelRequest() {
        requestId?.let { id ->
            viewModel.cancelRequest(id)
            Toast.makeText(this, "Request canceled", Toast.LENGTH_SHORT).show()
            finish()
        } ?: showError("Request ID is missing.", false)
    }

    private fun onAssistantSelected(assistantUserId: Assistant) {
        // Handle assistant selection, e.g., show details or navigate
        Toast.makeText(this, "Assistant selected: $assistantUserId", Toast.LENGTH_SHORT).show()
    }
}




