package com.example.laptrack.app.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun Logo(size: androidx.compose.ui.unit.Dp, @DrawableRes imageRes: Int) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size / 4))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "App Logo",
            modifier = Modifier
                .padding(size / 8)
        )
    }
}
