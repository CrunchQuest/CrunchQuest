package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.BookingMapper
import com.crunchquest.android.data.source.local.BookingLocalDataSource
import com.crunchquest.android.data.source.remote.BookingRemoteDataSource
import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val remoteDataSource: BookingRemoteDataSource,
    private val localDataSource: BookingLocalDataSource,
    private val bookingMapper: BookingMapper
) : BookingRepository {

    // Create a booking
    override suspend fun createBooking(booking: Booking): Result<Booking> {
        return try {
            // Convert the domain entity to the remote data model
            val bookingRemote = bookingMapper.toRemote(booking)

            // Send the booking to the backend
            val createdRemoteBooking = remoteDataSource.createBooking(bookingRemote)

            // Convert the response from the backend to the domain entity
            val createdBooking = bookingMapper.fromRemote(createdRemoteBooking)

            // Save the booking locally for offline access
            localDataSource.saveBooking(bookingMapper.toLocal(createdBooking))

            Result.Success(createdBooking)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Get a booking by booking ID
    override suspend fun getBooking(bookingId: String): Result<List<Booking>> {
        return try {
            // Attempt to fetch from local cache first
            val bookingLocal = localDataSource.getBooking(bookingId)
            if (bookingLocal != null) {
                return Result.Success(listOf(bookingMapper.fromLocal(bookingLocal)))
            }

            // Fetch from remote if local data is not found
            val bookingRemote = remoteDataSource.getBooking(bookingId)
            if (bookingRemote != null) {
                val booking = bookingMapper.fromRemote(bookingRemote)
                localDataSource.saveBooking(bookingMapper.toLocal(booking)) // Cache locally
                Result.Success(listOf(booking))
            } else {
                Result.Error(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getBookingsByStatus(status: String): Result<List<Booking>> {
        return try {
            val remoteBookings = remoteDataSource.getBookingsByStatus(status)
            val bookings = remoteBookings.map { bookingMapper.fromRemote(it) }
            bookings.forEach { booking ->
                localDataSource.saveBooking(bookingMapper.toLocal(booking)) // Cache locally
            }
            Result.Success(bookings)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Get bookings by customer ID
    override suspend fun getBookingsByCustomer(customerId: String): Result<List<Booking>> {
        return try {
            // Attempt to get bookings from local cache first
            val localBookings = localDataSource.getBookingsByCustomer(customerId).map { bookingMapper.fromLocal(it) }
            if (localBookings.isNotEmpty()) {
                return Result.Success(localBookings)
            }

            // Fetch from remote if local data is empty
            val remoteBookings = remoteDataSource.getBookingsByCustomer(customerId)?.map { bookingMapper.fromRemote(it) }
            if (remoteBookings != null && remoteBookings.isNotEmpty()) {
                remoteBookings.forEach { booking ->
                    localDataSource.saveBooking(bookingMapper.toLocal(booking)) // Cache each booking locally
                }
                Result.Success(remoteBookings)
            } else {
                Result.Error(Exception("No bookings found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Update a booking
    override suspend fun updateBooking(booking: Booking): Result<Unit> {
        return try {
            val bookingRemote = bookingMapper.toRemote(booking)
            remoteDataSource.updateBooking(bookingRemote)
            localDataSource.updateBooking(bookingMapper.toLocal(booking)) // Update locally
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Delete a booking
    override suspend fun deleteBooking(bookingId: String): Result<Unit> {
        return try {
            val bookingLocal = localDataSource.getBooking(bookingId)
            if (bookingLocal != null) {
                remoteDataSource.deleteBooking(bookingId)
                localDataSource.deleteBooking(bookingLocal) // Delete locally
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Booking not found in local storage"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}



