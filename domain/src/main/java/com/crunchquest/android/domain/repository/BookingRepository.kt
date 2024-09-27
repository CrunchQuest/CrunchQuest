package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.utility.Result

interface BookingRepository {
    suspend fun createBooking(booking: Booking): Result<Booking>
    suspend fun getBooking(bookingId: String): Result<List<Booking>>
    suspend fun getBookingsByStatus(status: String): Result<List<Booking>>
    suspend fun getBookingsByCustomer(customerId: String): Result<List<Booking>>
    suspend fun updateBooking(booking: Booking): Result<Unit>
    suspend fun deleteBooking(bookingId: String): Result<Unit>
}
