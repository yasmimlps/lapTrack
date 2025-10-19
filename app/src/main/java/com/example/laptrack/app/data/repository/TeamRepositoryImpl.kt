package com.example.laptrack.app.data.repository

import android.util.Log
import com.example.laptrack.app.data.source.local.TeamLocalDataSource
import com.example.laptrack.app.data.source.remote.GoogleSheetDataSource
import com.example.laptrack.app.domain.model.Team
import com.example.laptrack.app.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class TeamRepositoryImpl (private val localDataSource: TeamLocalDataSource, private val remoteDataSource: GoogleSheetDataSource) : TeamRepository {

    private val TAG = "TeamRepositoryImpl"

    private val _teams = MutableStateFlow<List<Team>>(localDataSource.getTeams())

    override fun getTeams(): Flow<List<Team>> {
        return _teams.asStateFlow()
    }

    override suspend fun getTeamById(teamId: Long): Team? {
        return _teams.first().find { it.id == teamId }
    }

    override suspend fun addTeam(name: String) {
        Log.d(TAG, "Adicionando equipe: $name")
        val newTeam = Team(name = name)
        val updatedTeams = _teams.value + newTeam
        localDataSource.saveTeams(updatedTeams)
        _teams.value = updatedTeams
        remoteDataSource.syncTeam(newTeam)
    }

    override suspend fun updateTeam(team: Team) {
        val currentTeams = _teams.value.toMutableList()
        val index = currentTeams.indexOfFirst { it.id == team.id }
        if(index != -1){
            currentTeams[index] = team
            localDataSource.saveTeams(currentTeams)
            _teams.value = currentTeams
            remoteDataSource.syncTeam(team)
        }
    }

    override suspend fun updateTeamStateInMemory(team: Team) {
        val currentTeams = _teams.value.toMutableList()
        val index = currentTeams.indexOfFirst { it.id == team.id }
        if(index != -1){
            currentTeams[index] = team
            _teams.value = currentTeams
        }
    }

    override suspend fun updateCurrentLapTime(teamId: Long, elapsed: Long) {
        val currentTeams = _teams.value.toMutableList()
        val index = currentTeams.indexOfFirst { it.id == teamId }
        if (index != -1) {
            val team = currentTeams[index]
            val updatedTeam = team.copy(currentLapTimeInMillis = team.currentLapTimeInMillis + elapsed)
            currentTeams[index] = updatedTeam
            _teams.value = currentTeams
        }
    }

}
