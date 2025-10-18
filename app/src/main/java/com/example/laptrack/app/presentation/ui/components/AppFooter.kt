package com.example.laptrack.app.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppFooter(onAddTeamClick: () -> Unit) {
    Box( modifier = Modifier
        .navigationBarsPadding()
        .padding( 16.dp)
        ) {
        Button(
            onClick = onAddTeamClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text("Cadastrar Equipe", fontWeight = FontWeight.Bold)
        }
    }
}