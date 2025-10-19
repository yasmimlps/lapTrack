package com.example.laptrack.app.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptrack.app.data.repository.TeamRepositoryImpl
import com.example.laptrack.app.data.source.local.TeamLocalDataSource
import com.example.laptrack.app.data.source.remote.GoogleSheetDataSource
import com.example.laptrack.app.domain.model.Team
import com.example.laptrack.app.domain.repository.TeamRepository
import com.example.laptrack.app.domain.usecase.AddLapUseCase
import com.example.laptrack.app.domain.usecase.TeamUseCases
import com.example.laptrack.app.domain.usecase.ToggleTimerUseCase
import com.example.laptrack.app.domain.usecase.UpdateLapTimeUseCase
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
        val dataSource = TeamLocalDataSource(application)
        val remoteDataSource = GoogleSheetDataSource()
        repository = TeamRepositoryImpl(dataSource, remoteDataSource)
        teamUseCases = TeamUseCases(
            addLap = AddLapUseCase(repository),
            toggleTimer = ToggleTimerUseCase(repository),
            updateLapTime = UpdateLapTimeUseCase(repository),
            finishedTeam = FinishTeamUseCase(repository),
        )

        repository.getTeams().onEach { teams ->
            _uiState.value = _uiState.value.copy(teams = teams)
        }.launchIn(viewModelScope)
    }


    fun onAddLap(teamId: Long) = viewModelScope.launch {
        teamUseCases.addLap(teamId)
    }

    fun onToggleTimer(teamId: Long) = viewModelScope.launch {
        teamUseCases.toggleTimer(teamId)
    }

    fun onUpdateLapTime(teamId: Long, elapsed: Long) = viewModelScope.launch {
        teamUseCases.updateLapTime(teamId, elapsed)
    }
    fun onFinishedTeam(teamId: Long) = viewModelScope.launch {
        teamUseCases.finishedTeam(teamId)
    }

    fun onAddTeamClicked() {
        _uiState.value = _uiState.value.copy(showAddTeamDialog = true)
    }

    fun onDismissDialog() {
        _uiState.value = _uiState.value.copy(showAddTeamDialog = false)
    }

    fun onConfirmTeam(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                Log.d("RaceViewModel", "onConfirmTeam chamada com o nome: $name")
                repository.addTeam(name)
            }
        }
        onDismissDialog()
    }
}


