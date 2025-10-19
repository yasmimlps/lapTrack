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

    // Lançador para o pedido de permissão de notificação.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Se a permissão for negada, exibe uma mensagem informativa.
            Toast.makeText(
                this,
                "A permissão para notificações é necessária para que o cronómetro funcione em segundo plano.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun askNotificationPermission() {
        // A permissão só é necessária no Android 13 (Tiramisu) ou superior.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verifica se a permissão ainda não foi concedida.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Lança o pedido de permissão.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pede a permissão assim que a atividade é criada.
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
    onAddTeamClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmTeam: (String) -> Unit
    // O onUpdateTime foi removido da assinatura da função.
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
                onAddTeamClicked = onAddTeamClicked,
                onDismissDialog = onDismissDialog,
                onConfirmTeam = onConfirmTeam
                // O onUpdateTime foi removido daqui.
            )
        }
    }
}
