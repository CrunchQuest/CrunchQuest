package com.crunchquest.android.presentation.ui.activity

import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.entities.Request

sealed class ActivityItem {

    data class RequestItem(val request: Request) : ActivityItem()
    data class AssistItem(val assistant: Assistant) : ActivityItem()
    data class BookingItem(val booking: Booking) : ActivityItem()

    // Helper function to retrieve the status for filtering
    fun getStatus(): String {
        return when (this) {
            is RequestItem -> request.status
            is AssistItem -> assistant.assistanceStatus
            is BookingItem -> booking.bookingStatus
        }
    }

    // Helper function to retrieve a timestamp for sorting
    fun getTimestamp(): String {
        return when (this) {
            is RequestItem -> request.updatedAt
            is AssistItem -> assistant.updatedAt
            is BookingItem -> booking.updatedAt
        }
    }
}
