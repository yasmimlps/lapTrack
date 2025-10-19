package com.example.laptrack.app.domain.usecase

import com.example.laptrack.app.domain.repository.TeamRepository

class ToggleTimerUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Long) {
        // Busca a equipe pelo ID fornecido.
        repository.getTeamById(teamId)?.let { team ->
            // Cria uma cópia da equipe, invertendo o valor de 'isRunning'.
            // Se estava 'true', vira 'false', e vice-versa.
            val updatedTeam = team.copy(isRunning = !team.isRunning)
            // Envia a equipe atualizada para o repositório salvar a alteração.
            repository.updateTeamStateInMemory(updatedTeam)
        }
    }
}