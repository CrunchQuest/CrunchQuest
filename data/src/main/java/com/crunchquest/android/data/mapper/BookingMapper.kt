package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.BookingRemote
import com.crunchquest.android.data.source.local.BookingLocal
import com.crunchquest.android.domain.entities.Booking
import javax.inject.Inject

class BookingMapper @Inject constructor(){

    fun fromRemote(bookingRemote: BookingRemote): Booking {
        return Booking(
            bookingId = bookingRemote.bookingId,
            customerId = bookingRemote.customerId,
            serviceId = bookingRemote.serviceId,
            providerId = bookingRemote.providerId,
            bookingDate = bookingRemote.bookingDate,
            bookingTime = bookingRemote.bookingTime,
            bookingStatus = bookingRemote.bookingStatus,
            notes = bookingRemote.notes,
            paymentStatus = bookingRemote.paymentStatus,
            totalPrice = bookingRemote.totalPrice,
            address = bookingRemote.address,
            captureResult = bookingRemote.captureResult,
            captureResultTimestamp = bookingRemote.captureResultTimestamp,
            createdAt = bookingRemote.createdAt,
            updatedAt = bookingRemote.updatedAt,
            providerConfirmed = bookingRemote.providerConfirmed,
            customerConfirmed = bookingRemote.customerConfirmed,
            reviewed = bookingRemote.reviewed
        )
    }

    fun toRemote(booking: Booking): BookingRemote {
        return BookingRemote(
            bookingId = booking.bookingId,
            customerId = booking.customerId,
            serviceId = booking.serviceId,
            providerId = booking.providerId,
            bookingDate = booking.bookingDate,
            bookingTime = booking.bookingTime,
            bookingStatus = booking.bookingStatus,
            notes = booking.notes,
            paymentStatus = booking.paymentStatus,
            totalPrice = booking.totalPrice,
            address = booking.address,
            captureResult = booking.captureResult,
            captureResultTimestamp = booking.captureResultTimestamp,
            createdAt = booking.createdAt,
            updatedAt = booking.updatedAt,
            providerConfirmed = booking.providerConfirmed,
            customerConfirmed = booking.customerConfirmed,
            reviewed = booking.reviewed
        )
    }

    fun fromLocal(bookingLocal: BookingLocal): Booking {
        return Booking(
            bookingId = bookingLocal.bookingId,
            customerId = bookingLocal.customerId,
            serviceId = bookingLocal.serviceId,
            providerId = bookingLocal.providerId,
            bookingDate = bookingLocal.bookingDate,
            bookingTime = bookingLocal.bookingTime,
            bookingStatus = bookingLocal.bookingStatus,
            notes = bookingLocal.notes,
            paymentStatus = bookingLocal.paymentStatus,
            totalPrice = bookingLocal.totalPrice,
            address = bookingLocal.address,
            captureResult = bookingLocal.captureResult,
            captureResultTimestamp = bookingLocal.captureResultTimestamp,
            createdAt = bookingLocal.createdAt,
            updatedAt = bookingLocal.updatedAt,
            providerConfirmed = bookingLocal.providerConfirmed,
            customerConfirmed = bookingLocal.customerConfirmed,
            reviewed = bookingLocal.reviewed
        )
    }

    fun toLocal(booking: Booking): BookingLocal {
        return BookingLocal(
            bookingId = booking.bookingId,
            customerId = booking.customerId,
            serviceId = booking.serviceId,
            providerId = booking.providerId,
            bookingDate = booking.bookingDate,
            bookingTime = booking.bookingTime,
            bookingStatus = booking.bookingStatus,
            notes = booking.notes,
            paymentStatus = booking.paymentStatus,
            totalPrice = booking.totalPrice,
            address = booking.address,
            captureResult = booking.captureResult,
            captureResultTimestamp = booking.captureResultTimestamp,
            createdAt = booking.createdAt,
            updatedAt = booking.updatedAt,
            providerConfirmed = booking.providerConfirmed,
            customerConfirmed = booking.customerConfirmed,
            reviewed = booking.reviewed
        )
    }
}
