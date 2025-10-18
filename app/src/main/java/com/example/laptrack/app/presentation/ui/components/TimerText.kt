package com.example.laptrack.app.presentation.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit

@Composable
fun TimerText(timeInMillis: Long) {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
    val millis = (timeInMillis % 1000) / 10

    Text(
        text = "%02d:%02d:%02d".format(minutes, seconds, millis),
        fontSize = 24.sp,
    )
}