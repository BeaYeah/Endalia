package com.beayeah.endalia.entities

data class Employee(
    val id: Int,
    val name: String,
    val lastName: String,
    val jobTitle: String,
    val phoneNumber: String,
    val email: String,
    val portrait: Int?
)