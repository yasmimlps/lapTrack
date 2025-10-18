package com.example.laptrack.app.domain.model

data class Team(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val laps: Int = 0,
    var totalTimeInMillis: Long = 0L,
    var currentLapTimeInMillis: Long = 0L,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false
)