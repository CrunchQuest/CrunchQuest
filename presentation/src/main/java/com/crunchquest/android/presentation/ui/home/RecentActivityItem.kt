package com.crunchquest.android.presentation.ui.home

import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.entities.Request

sealed class RecentActivityItem {
    data class RequestItem(val request: Request) : RecentActivityItem()
    data class BookingItem(val booking: Booking) : RecentActivityItem()

    fun getTimestamp(): String {
        return when (this) {
            is RequestItem -> request.updatedAt
            is BookingItem -> booking.updatedAt
        }
    }
}
