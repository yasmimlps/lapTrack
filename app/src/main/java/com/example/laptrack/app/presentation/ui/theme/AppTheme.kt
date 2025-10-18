package com.example.laptrack.app.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF111827), // bg-gray-900
    surface = Color(0xFF1F2937),   // bg-gray-800
    onSurface = Color.White,
    primary = Color(0xFFF59E0B),    // bg-yellow-500
    onPrimary = Color(0xFF111827),  // text-gray-900
    secondary = Color(0xFF4B5563), // bg-gray-600
    onSecondary = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
