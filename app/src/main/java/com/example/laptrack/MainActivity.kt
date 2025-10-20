package com.example.laptrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.laptrack.app.presentation.RaceViewModel
import com.example.laptrack.app.presentation.ui.screens.AppScreen
import com.example.laptrack.app.presentation.ui.screens.HomeScreen
import com.example.laptrack.app.presentation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: RaceViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "A permissão de notificação é necessária para que o cronômetro funcione em segundo plano.", Toast.LENGTH_LONG).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

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
                        onFinishTeam = viewModel::onFinishTeam,
                        onStartRace = viewModel::onStartRace,
                        onStopRace = viewModel::onStopRace,
                        onAddTeamClicked = viewModel::onAddTeamClicked,
                        onDismissDialog = viewModel::onDismissDialog,
                        onConfirmTeam = viewModel::onConfirmTeam
                    )
                }
            }
        }
    }
}

enum class Screen { Home, App }

@Composable
fun AppCapKaraNav(
    state: com.example.laptrack.app.presentation.RaceUiState,
    onAddLap: (Long) -> Unit,
    onToggleTimer: (Long) -> Unit,
    onFinishTeam: (Long) -> Unit,
    onStartRace: () -> Unit,
    onStopRace: () -> Unit,
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
                onFinishTeam = onFinishTeam,
                onStartRace = onStartRace,
                onStopRace = onStopRace,
                onAddTeamClicked = onAddTeamClicked,
                onDismissDialog = onDismissDialog,
                onConfirmTeam = onConfirmTeam
            )
        }
    }
}
