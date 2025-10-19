package com.example.laptrack.app.presentation

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptrack.app.data.repository.TeamRepositoryImpl
import com.example.laptrack.app.domain.model.Team
import com.example.laptrack.app.domain.repository.TeamRepository
import com.example.laptrack.app.domain.usecase.AddLapUseCase
import com.example.laptrack.app.domain.usecase.TeamUseCases
import com.example.laptrack.app.domain.usecase.ToggleTimerUseCase
import com.example.laptrack.app.domain.usecase.FinishTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class RaceUiState(
    val teams: List<Team> = emptyList(),
    val showAddTeamDialog: Boolean = false
)

class RaceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TeamRepository
    private val teamUseCases: TeamUseCases

    private val _uiState = MutableStateFlow(RaceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        repository = TeamRepositoryImpl.getInstance(application)

        teamUseCases = TeamUseCases(
            addLap = AddLapUseCase(repository),
            toggleTimer = ToggleTimerUseCase(repository),
            finishedTeam = FinishTeamUseCase(repository)
        )

        repository.getTeams().onEach { teams ->
            Log.d("RaceViewModel", "Recebida nova lista de equipes. A primeira equipe correndo tem o tempo: ${teams.find { it.isRunning }?.currentLapTimeInMillis}")
            _uiState.value = _uiState.value.copy(teams = teams)
        }.launchIn(viewModelScope)
    }

    // LÓGICA SIMPLIFICADA E MAIS ROBUSTA
    fun onToggleTimer(teamId: Long) = viewModelScope.launch {
        // 1. Altera o estado da equipe (Pausar/Retomar)
        teamUseCases.toggleTimer(teamId)

        // 2. Apenas garante que o serviço seja iniciado.
        // Se já estiver rodando, não há problema. Se não estiver, ele começará.
        // A lógica para parar o serviço agora está dentro do próprio serviço, que é mais confiável.
        Log.d("RaceViewModel", "Garantindo que o serviço de cronômetro está iniciado.")
        val intent = Intent(getApplication(), RaceTimerService::class.java)
        getApplication<Application>().startService(intent)
    }

    fun onAddLap(teamId: Long) = viewModelScope.launch { teamUseCases.addLap(teamId) }

    fun onFinishTeam(teamId: Long) = viewModelScope.launch {
        teamUseCases.finishedTeam(teamId)
        // Após finalizar, também garantimos que o serviço verifique seu estado.
        val intent = Intent(getApplication(), RaceTimerService::class.java)
        getApplication<Application>().startService(intent)
    }

    fun onAddTeamClicked() { _uiState.value = _uiState.value.copy(showAddTeamDialog = true) }
    fun onDismissDialog() { _uiState.value = _uiState.value.copy(showAddTeamDialog = false) }
    fun onConfirmTeam(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch { repository.addTeam(name) }
        }
        onDismissDialog()
    }
}
