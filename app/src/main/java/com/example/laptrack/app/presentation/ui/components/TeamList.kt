package com.example.laptrack.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptrack.app.domain.model.Team

@Composable
fun TeamList(
    teams: List<Team>,
    onAddLap: (Long) -> Unit,
    onToggleTimer: (Long) -> Unit,
    onUpdateTime: (Long, Long) -> Unit, // Parâmetro adicionado
    onFinishTeam: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (teams.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhuma equipe cadastrada.\nClique em \"Cadastrar Equipe\" para começar.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(teams, key = { it.id }) { team ->
                TeamCard(
                    team = team,
                    onAddLap = { onAddLap(team.id) },
                    onToggleTimer = { onToggleTimer(team.id) },
                    onUpdateTime = { elapsed -> onUpdateTime(team.id, elapsed) } ,
                    onFinishTeam = { onFinishTeam(team.id) }
                )
            }
        }
    }
}