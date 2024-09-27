package com.crunchquest.android.presentation.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.presentation.ui.request.RequestDetailActivity
import com.crunchquest.android.presentation.utility.DateUtils
import com.crunchquest.shared.databinding.ItemRequestBinding

class RequestAdapter : ListAdapter<Request, RequestAdapter.RequestViewHolder>(RequestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = getItem(position)
        holder.bind(request)
    }

    class RequestViewHolder(private val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: Request) {
            binding.requestTitle.text = request.title
            binding.requestDescription.text = request.description
            binding.rewardChip.text = request.rewards.toString()
            binding.statusChip.text = request.status

            val date = DateUtils.parseDate(request.createdAt)
            val formattedDate = date?.let { DateUtils.formatDate(it) } ?: "Invalid date"
            binding.dateChip.text = formattedDate
            // Bind other UI elements as needed

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, RequestDetailActivity::class.java).apply {
                    putExtra("REQUEST_ID", request.requestId) // Pass the request ID or other necessary data
                }
                context.startActivity(intent) // Start the RequestDetailActivity
            }
        }
    }
}

class RequestDiffCallback : DiffUtil.ItemCallback<Request>() {
    override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem.requestId == newItem.requestId
    }

    override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
        return oldItem == newItem
    }
}
