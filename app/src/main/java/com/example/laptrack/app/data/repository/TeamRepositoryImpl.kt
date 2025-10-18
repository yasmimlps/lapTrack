package com.example.laptrack.app.data.repository

import android.util.Log
import com.example.laptrack.app.data.source.local.TeamLocalDataSource
import com.example.laptrack.app.domain.model.Team
import com.example.laptrack.app.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class TeamRepositoryImpl (private val localDataSource: TeamLocalDataSource) : TeamRepository {

    private val TAG = "TeamRepositoryImpl"

    // Inicializa o StateFlow com a lista salva no SharedPreferences
    private val _teams = MutableStateFlow<List<Team>>(localDataSource.getTeams())

    override fun getTeams(): Flow<List<Team>> {
        // Retorna o fluxo para que a UI possa observá-lo
        return _teams.asStateFlow()
    }

    // CORRIGIDO: Implementação do método que estava faltando
    override suspend fun getTeamById(teamId: Long): Team? {
        // Pega a lista mais recente do fluxo e procura a equipe pelo ID
        return _teams.first().find { it.id == teamId }
    }


    // CORRIGIDO: A função recebe o NOME da equipe (String) e cria o objeto Team.
    override suspend fun addTeam(name: String) {
        Log.d(TAG, "Adicionando equipe: $name")
        // Cria um novo objeto Team com o nome fornecido
        val newTeam = Team(name = name)
        // Adiciona a nova equipe à lista atual
        val updatedTeams = _teams.value + newTeam
        // Salva a lista atualizada no SharedPreferences
        localDataSource.saveTeams(updatedTeams)
        // Emite a nova lista para os observadores (UI)
        _teams.value = updatedTeams
    }

    override suspend fun updateTeam(team: Team) {
        val currentTeams = _teams.value.toMutableList()
        val index = currentTeams.indexOfFirst { it.id == team.id }
        if(index != -1){
            currentTeams[index] = team
            localDataSource.saveTeams(currentTeams)
            _teams.value = currentTeams
        }
    }
}
