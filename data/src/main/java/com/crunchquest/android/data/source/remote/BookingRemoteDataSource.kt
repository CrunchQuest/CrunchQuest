package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.BookingRemote
import com.crunchquest.android.data.remote.api.ApiService
import com.crunchquest.android.domain.utility.Result

class BookingRemoteDataSource(private val apiService: ApiService) {

    // Function to create a booking on the backend
    suspend fun createBooking(bookingRemote: BookingRemote): BookingRemote {
        val response = apiService.createBooking(bookingRemote)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to create booking: ${response.message()}")
        }
    }

    // Function to get a booking by bookingId
    suspend fun getBooking(bookingId: String): BookingRemote? {
        val response = apiService.getBooking(bookingId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getBookingsByStatus(status: String): List<BookingRemote> {
        val response = apiService.getBookings(status)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch bookings: ${response.message()}")
        }
    }

    // Function to get bookings by customerId
    suspend fun getBookingsByCustomer(customerId: String): List<BookingRemote> {
        val response = apiService.getBookingByCustomer(customerId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch bookings: ${response.message()}")
        }
    }

    // Function to update an existing booking
    suspend fun updateBooking(bookingRemote: BookingRemote) {
        val response = apiService.updateBooking(bookingRemote.bookingId, bookingRemote)
        if (!response.isSuccessful) {
            throw Exception("Failed to update booking: ${response.message()}")
        }
    }

    // Function to delete a booking by bookingId
    suspend fun deleteBooking(bookingId: String) {
        val response = apiService.deleteBooking(bookingId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete booking: ${response.message()}")
        }
    }
}
