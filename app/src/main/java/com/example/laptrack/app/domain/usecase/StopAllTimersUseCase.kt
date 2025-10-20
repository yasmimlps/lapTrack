package com.example.laptrack.app.domain.usecase

import com.example.laptrack.app.domain.repository.TeamRepository

class StopAllTimersUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke() {
        repository.stopAllTimers()
    }
}
