package com.crunchquest.android.domain.usecase.booking

import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.repository.BookingRepository
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(booking: Booking): Result<Booking> {
        return bookingRepository.createBooking(booking)
    }
}
