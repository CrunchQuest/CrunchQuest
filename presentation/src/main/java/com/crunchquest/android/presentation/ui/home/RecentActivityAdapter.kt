package com.crunchquest.android.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.shared.databinding.ItemRecentBookingBinding
import com.crunchquest.shared.databinding.ItemRecentRequestBinding

class RecentActivityAdapter : ListAdapter<RecentActivityItem, RecyclerView.ViewHolder>(RecentActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_REQUEST -> {
                val binding = ItemRecentRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RequestViewHolder(binding)
            }
            VIEW_TYPE_BOOKING -> {
                val binding = ItemRecentBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BookingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecentActivityItem.RequestItem -> (holder as RequestViewHolder).bind(item.request)
            is RecentActivityItem.BookingItem -> (holder as BookingViewHolder).bind(item.booking)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecentActivityItem.RequestItem -> VIEW_TYPE_REQUEST
            is RecentActivityItem.BookingItem -> VIEW_TYPE_BOOKING
        }
    }

    class RequestViewHolder(private val binding: ItemRecentRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: Request) {
            binding.requestTitle.text = request.title
            binding.requestDescription.text = request.description
            // Bind other UI elements as needed
        }
    }

    class BookingViewHolder(private val binding: ItemRecentBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.bookingStatus.text = booking.bookingStatus
            binding.bookingDate.text = booking.bookingDate
            // Bind other UI elements as needed
        }
    }

    companion object {
        private const val VIEW_TYPE_REQUEST = 0
        private const val VIEW_TYPE_BOOKING = 1
    }
}

class RecentActivityDiffCallback : DiffUtil.ItemCallback<RecentActivityItem>() {
    override fun areItemsTheSame(oldItem: RecentActivityItem, newItem: RecentActivityItem): Boolean {
        return when {
            oldItem is RecentActivityItem.RequestItem && newItem is RecentActivityItem.RequestItem -> oldItem.request.requestId == newItem.request.requestId
            oldItem is RecentActivityItem.BookingItem && newItem is RecentActivityItem.BookingItem -> oldItem.booking.bookingId == newItem.booking.bookingId
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecentActivityItem, newItem: RecentActivityItem): Boolean {
        return oldItem == newItem
    }
}
