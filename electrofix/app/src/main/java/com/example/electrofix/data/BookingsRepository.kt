package com.example.electrofix.data

import javax.inject.Inject

enum class BookingStatus { ONGOING, COMPLETED, CANCELLED }

data class Booking(
    val id: String,
    val technicianName: String,
    val dateTime: String,
    val status: BookingStatus
)

class BookingsRepository @Inject constructor() {
    fun getBookings(): List<Booking> {
        return listOf(
            Booking("1", "John Doe", "2024-07-28 at 10:00 AM", BookingStatus.ONGOING),
            Booking("2", "Jane Smith", "2024-07-27 at 2:00 PM", BookingStatus.COMPLETED),
            Booking("3", "Peter Jones", "2024-07-26 at 12:00 PM", BookingStatus.CANCELLED),
            Booking("4", "Mary Williams", "2024-07-29 at 4:00 PM", BookingStatus.ONGOING),
        )
    }
}