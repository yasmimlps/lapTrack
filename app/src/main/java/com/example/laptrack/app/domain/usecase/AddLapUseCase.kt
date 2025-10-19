package com.example.laptrack.app.domain.usecase

import androidx.room.util.copy
import com.example.laptrack.app.domain.repository.TeamRepository

class AddLapUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long) {
        repository.recordLap(teamId)
    }
}