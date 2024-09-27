package com.crunchquest.android.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.shared.databinding.ItemAssistantBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssistantAdapter(
    private val getUserById: suspend (String) -> com.crunchquest.android.domain.utility.Result<User>, // Fetches user data based on AssistantUserId
    private val onAssistantClick: (Assistant) -> Unit // Handles assistant item clicks
) : ListAdapter<Assistant, AssistantAdapter.AssistantViewHolder>(AssistantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistantViewHolder {
        val binding = ItemAssistantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssistantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssistantViewHolder, position: Int) {
        val assistant = getItem(position)
        holder.bind(assistant)
    }

    inner class AssistantViewHolder(private val binding: ItemAssistantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(assistant: Assistant) {
            // Display placeholder data initially
            binding.assistantName.text = "Loading..."
            binding.assistantStatus.text = assistant.assistanceStatus

            // Load actual user data asynchronously
            CoroutineScope(Dispatchers.Main).launch {
                // Fetch user data based on the assistant's user ID
                val result = withContext(Dispatchers.IO) { getUserById(assistant.assistantUserId) }
                handleUserResult(result)
            }

            // Set click listener
            binding.root.setOnClickListener {
                onAssistantClick(assistant)
            }
        }

        private fun handleUserResult(result: com.crunchquest.android.domain.utility.Result<User>) {
            when (result) {
                is com.crunchquest.android.domain.utility.Result.Success -> {
                    val user = result.data
                    binding.assistantName.text = "${user.firstName} ${user.lastName}".takeIf { it.isNotBlank() } ?: "No Name"
                }
                is com.crunchquest.android.domain.utility.Result.Error -> {
                    binding.assistantName.text = "Unknown Assistant"
                    binding.assistantStatus.text = "Error loading assistant"
                }
                com.crunchquest.android.domain.utility.Result.Loading -> {
                    binding.assistantName.text = "Loading..."
                    binding.assistantStatus.text = "Fetching assistant data..."
                }
            }
        }
    }
}

class AssistantDiffCallback : DiffUtil.ItemCallback<Assistant>() {
    override fun areItemsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
        return oldItem.assistantUserId == newItem.assistantUserId
    }

    override fun areContentsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
        return oldItem == newItem
    }
}




