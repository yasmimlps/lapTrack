package com.example.laptrack.app.domain.usecase

import com.example.laptrack.app.domain.repository.TeamRepository

class UpdateLapTimeUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long, elapsed: Long) {
        // Busca a equipe pelo ID fornecido
        repository.getTeamById(teamId)?.let { team ->
            // Cria uma cópia da equipe, somando o tempo decorrido ('elapsed')
            // ao tempo da volta atual ('currentLapTimeInMillis').
            val updatedTeam = team.copy(currentLapTimeInMillis = team.currentLapTimeInMillis + elapsed)
            // Envia a equipe atualizada para o repositório salvar a alteração.
            repository.updateTeam(updatedTeam)
        }
    }
}