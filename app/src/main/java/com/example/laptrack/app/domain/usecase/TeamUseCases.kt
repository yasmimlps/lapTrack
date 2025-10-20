package com.example.laptrack.app.domain.usecase

data class TeamUseCases(
    val addLap: AddLapUseCase,
    val toggleTimer: ToggleTimerUseCase,
    val finishedTeam: FinishTeamUseCase,
    val startAllTimers: StartAllTimersUseCase,
    val stopAllTimers: StopAllTimersUseCase
)

