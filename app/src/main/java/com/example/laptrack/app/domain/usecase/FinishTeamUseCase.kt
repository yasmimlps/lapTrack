package com.example.laptrack.app.domain.usecase

import com.example.laptrack.app.domain.repository.TeamRepository

class FinishTeamUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long) {
        val team = repository.getTeamById(teamId) ?: return
        if (!team.isFinished) {
            val finishedTeam = team.copy(
                isFinished = true,
                isRunning = false // Para o cronômetro ao finalizar
            )
            repository.updateTeam(finishedTeam)
        }
    }
}
