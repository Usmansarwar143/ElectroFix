package com.example.electrofix.data

import javax.inject.Inject

class HomeRepository @Inject constructor() {
    // Dummy data
    fun getServiceCategories(): List<String> {
        return listOf("Electrician", "AC Repair", "UPS Repair", "Plumber", "Carpenter", "Painter")
    }

    fun getNearbyTechnicians(): List<String> {
        return listOf("John Doe", "Jane Smith", "Peter Jones", "Mary Williams")
    }
}