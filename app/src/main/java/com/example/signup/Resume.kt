package com.example.signup

data class Resume(
    val name: String,
    val skills: List<String>,
    val projects: List<Project>,
    val address: String,
    val email: String,
    val phone: String,
    val summary: String,
    val twitter: String
)


