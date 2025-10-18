package com.example.laptrack.app.domain.usecase

data class TeamUseCases(
    val addLap: AddLapUseCase,
    val toggleTimer: ToggleTimerUseCase,
    val updateLapTime: UpdateLapTimeUseCase,
    val finishedTeam: FinishTeamUseCase
)

