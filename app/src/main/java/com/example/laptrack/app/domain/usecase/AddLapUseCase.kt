package com.example.laptrack.app.domain.usecase

import androidx.room.util.copy
import com.example.laptrack.app.domain.repository.TeamRepository

class AddLapUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long) {
        repository.getTeamById(teamId)?.let { team ->
            val updatedTeam = team.copy(
                laps = team.laps + 1,
                totalTimeInMillis = team.totalTimeInMillis + team.currentLapTimeInMillis,
                currentLapTimeInMillis = 0L,
                isRunning = true
            )
            repository.updateTeam(updatedTeam)
        }
    }
}