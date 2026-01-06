package com.example.electrofix.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "",
    val name: String = "",
    val phone: String = "",
    val isPremium: Boolean = false
)