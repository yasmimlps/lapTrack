package com.example.laptrack.app.presentation.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit

@Composable
fun TimerText(timeInMillis: Long) {
    // LÓGICA DE FORMATAÇÃO ATUALIZADA
    val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60

    val text = if (hours > 0) {
        // Se houver horas, mostra HH:MM:SS
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        // Caso contrário, mostra apenas MM:SS
        "%02d:%02d".format(minutes, seconds)
    }

    Text(
        text = text,
        fontSize = 22.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium
    )
}
