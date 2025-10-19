package com.example.laptrack.app.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptrack.app.domain.model.Team
import kotlinx.coroutines.delay

@Composable
fun TeamCard(
    team: Team,
    onAddLap: () -> Unit,
    onToggleTimer: () -> Unit,
    onFinishTeam: () -> Unit
    // O parâmetro onUpdateTime foi removido
) {
    val isEnabled = !team.isFinished
    val alpha: Float by animateFloatAsState(if (isEnabled) 1f else 0.6f, label = "")

    // O LaunchedEffect foi completamente removido daqui.
    // O serviço em segundo plano agora é responsável por atualizar o tempo.

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.alpha(alpha)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            // Seção: Nome e Contador de Voltas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(team.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Column(horizontalAlignment = Alignment.End) {
                    Text("Volta", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
                    Text(team.laps.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))

            // Seção: Cronômetros e Botão "Adicionar Volta"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Coluna dos tempos
                Column(modifier = Modifier.clickable(enabled = isEnabled, onClick = onToggleTimer)) {
                    Text("Tempo da Volta", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
                    TimerText(timeInMillis = team.currentLapTimeInMillis)
                    Spacer(Modifier.height(8.dp))
                    Text("Tempo Total", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
                    TimerText(timeInMillis = team.totalTimeInMillis)
                }
                // Botão principal
                Button(
                    onClick = onAddLap,
                    enabled = isEnabled,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("Adicionar volta", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text("+", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
            }

            // Seção dos botões de Pausar e Finalizar
            AnimatedVisibility(visible = isEnabled) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão de Pausar/Retomar
                    OutlinedButton(
                        onClick = onToggleTimer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (team.isRunning) "Pausar" else "Iniciar")
                    }
                    // Botão de Finalizar
                    TextButton(onClick = onFinishTeam) {
                        Text("Finalizar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
