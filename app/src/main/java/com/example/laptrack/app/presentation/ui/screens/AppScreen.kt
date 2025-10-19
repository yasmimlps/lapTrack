package com.example.laptrack.app.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.laptrack.app.presentation.RaceUiState
import com.example.laptrack.app.presentation.ui.components.AddTeamDialog
import com.example.laptrack.app.presentation.ui.components.AppFooter
import com.example.laptrack.app.presentation.ui.components.AppHeader
import com.example.laptrack.app.presentation.ui.components.TeamList

@Composable
fun AppScreen(
    state: RaceUiState,
    onAddLap: (Long) -> Unit,
    onToggleTimer: (Long) -> Unit,
    onFinishTeam: (Long) -> Unit,
    onAddTeamClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmTeam: (String) -> Unit
) {
    Scaffold(
        topBar = { AppHeader() },
        bottomBar = { AppFooter(onAddTeamClick = onAddTeamClicked) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        TeamList(
            teams = state.teams,
            onAddLap = onAddLap,
            onToggleTimer = onToggleTimer,

            onFinishTeam = onFinishTeam,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (state.showAddTeamDialog) {
        AddTeamDialog(
            onDismiss = onDismissDialog,
            onConfirm = onConfirmTeam
        )
    }
}

