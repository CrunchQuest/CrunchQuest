package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "bookings")
data class BookingLocal(
    @PrimaryKey val bookingId: String,
    val customerId: String,
    val serviceId: String,
    val providerId: String,
    val bookingDate: String,
    val bookingTime: String,
    val bookingStatus: String,
    val notes: String?,
    val paymentStatus: String,
    val totalPrice: Double,
    val address: String,
    val captureResult: String?,
    val captureResultTimestamp: String?,
    val createdAt: String,
    val updatedAt: String,
    val providerConfirmed: Boolean,
    val customerConfirmed: Boolean,
    val reviewed: Boolean
)

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingLocal)

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    suspend fun getBooking(bookingId: String): BookingLocal?

    @Query("SELECT * FROM bookings WHERE customerId = :customerId")
    suspend fun getBookingsByCustomer(customerId: String): List<BookingLocal>

    @Update
    suspend fun updateBooking(booking: BookingLocal)

    @Delete
    suspend fun deleteBooking(booking: BookingLocal)
}

class BookingLocalDataSource(private val bookingDao: BookingDao) {

    suspend fun saveBooking(bookingLocal: BookingLocal) {
        bookingDao.insertBooking(bookingLocal)
    }

    suspend fun getBooking(bookingId: String): BookingLocal? {
        return bookingDao.getBooking(bookingId)
    }

    suspend fun getBookingsByCustomer(customerId: String): List<BookingLocal> {
        return bookingDao.getBookingsByCustomer(customerId)
    }

    suspend fun updateBooking(bookingLocal: BookingLocal) {
        bookingDao.updateBooking(bookingLocal)
    }

    suspend fun deleteBooking(bookingLocal: BookingLocal) {
        bookingDao.deleteBooking(bookingLocal)
    }
}
