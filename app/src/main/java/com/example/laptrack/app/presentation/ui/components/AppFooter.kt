package com.example.laptrack.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppFooter(
    isRaceInProgress: Boolean,
    onStartRace: () -> Unit,
    onStopRace: () -> Unit,
    onAddTeamClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botão de controlo global
        Button(
            onClick = { if (isRaceInProgress) onStopRace() else onStartRace() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRaceInProgress) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (isRaceInProgress) "Parar Prova" else "Iniciar Prova", fontWeight = FontWeight.Bold)
        }

        // Botão de adicionar equipa
        OutlinedButton(
            onClick = onAddTeamClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text("Cadastrar Equipe")
        }
    }
}