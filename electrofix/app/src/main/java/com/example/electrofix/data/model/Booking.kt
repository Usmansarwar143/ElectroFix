
package com.example.electrofix.data.model

data class Booking(
    val bookingId: String = "",
    val service: String = "",
    val description: String = "",
    val status: String = "",
    val cost: Double = 0.0,
    val customerId: String = "",
    val technicianId: String = ""
)
