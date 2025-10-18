package com.example.laptrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.laptrack.app.presentation.RaceViewModel
import com.example.laptrack.app.presentation.ui.screens.AppScreen
import com.example.laptrack.app.presentation.ui.screens.HomeScreen
import com.example.laptrack.app.presentation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: RaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    AppCapKaraNav(
                        state = uiState,
                        onAddLap = viewModel::onAddLap,
                        onToggleTimer = viewModel::onToggleTimer,
                        onUpdateTime = viewModel::onUpdateLapTime, // Função conectada aqui
                        onFinishTeam = viewModel::onFinishedTeam,
                        onAddTeamClicked = viewModel::onAddTeamClicked,
                        onDismissDialog = viewModel::onDismissDialog,
                        onConfirmTeam = viewModel::onConfirmTeam
                    )
                }
            }
        }
    }
}

// Navegação simples entre as telas
enum class Screen {
    Home, App
}

@Composable
fun AppCapKaraNav(
    state: com.example.laptrack.app.presentation.RaceUiState,
    onAddLap: (Long) -> Unit,
    onToggleTimer: (Long) -> Unit,
    onUpdateTime: (Long, Long) -> Unit,
    onFinishTeam: (Long) -> Unit,
    onAddTeamClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmTeam: (String) -> Unit
) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() }, label = ""
    ) { screen ->
        when (screen) {
            Screen.Home -> HomeScreen(onStartClick = { currentScreen = Screen.App })
            Screen.App -> AppScreen(
                state = state,
                onAddLap = onAddLap,
                onToggleTimer = onToggleTimer,
                onUpdateTime = onUpdateTime,
                onFinishTeam = onFinishTeam,
                onAddTeamClicked = onAddTeamClicked,
                onDismissDialog = onDismissDialog,
                onConfirmTeam = onConfirmTeam
            )
        }
    }
}