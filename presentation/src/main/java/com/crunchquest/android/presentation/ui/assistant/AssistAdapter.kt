package com.crunchquest.android.presentation.ui.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.shared.databinding.ItemAssistBinding

class AssistAdapter : ListAdapter<Assistant, AssistAdapter.AssistViewHolder>(AssistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistViewHolder {
        val binding = ItemAssistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssistViewHolder, position: Int) {
        val assist = getItem(position)
        holder.bind(assist)
    }

    class AssistViewHolder(private val binding: ItemAssistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(assistant: Assistant) {
            // Bind Assistant data to the view elements
            binding.assistTitle.text = "Assistant ID: ${assistant.assistantId}" // Example binding
            binding.assistStatus.text = assistant.assistanceStatus
            binding.assistRewards.text = "Proposed Rewards: ${assistant.proposedRewards ?: "N/A"}"

            // Bind other UI elements as needed
        }
    }
}

class AssistDiffCallback : DiffUtil.ItemCallback<Assistant>() {
    override fun areItemsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
        return oldItem.assistantId == newItem.assistantId
    }

    override fun areContentsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
        return oldItem == newItem
    }
}
