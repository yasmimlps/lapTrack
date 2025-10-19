package com.example.laptrack.app.domain.usecase

import com.example.laptrack.app.domain.repository.TeamRepository

class UpdateLapTimeUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long, elapsed: Long) {
        repository.updateCurrentLapTime(teamId, elapsed)
    }
}