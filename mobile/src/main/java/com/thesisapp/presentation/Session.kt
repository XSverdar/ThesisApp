package com.thesisapp.presentation

// TODO: Change the variables
data class Session(
    val date: String,
    val time: String,
    val startTime: String,
    val endTime: String,
    val distance: String = "Unknown",
    val stroke: String = "Unknown",
    val pace: String = "Unknown",
    val notes: String = ""
)