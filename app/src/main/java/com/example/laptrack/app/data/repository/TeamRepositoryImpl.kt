package com.example.laptrack.app.data.repository

import android.content.Context
import android.util.Log
import com.example.laptrack.app.data.source.local.TeamLocalDataSource
import com.example.laptrack.app.data.source.remote.GoogleSheetDataSource
import com.example.laptrack.app.domain.model.Team
import com.example.laptrack.app.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class TeamRepositoryImpl (private val localDataSource: TeamLocalDataSource,
                          private val remoteDataSource: GoogleSheetDataSource) : TeamRepository {

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
        val updatedTeams = _teams.value.map { if (it.id == team.id) team else it }
        localDataSource.saveTeams(updatedTeams)
        _teams.value = updatedTeams
        remoteDataSource.syncTeam(team)
    }

    override suspend fun updateTeamStateInMemory(team: Team) {
        val updatedTeams = _teams.value.map { if (it.id == team.id) team else it }
        _teams.value = updatedTeams
    }

    override suspend fun updateAllRunningTimers(elapsed: Long) {
        val updatedTeams = _teams.value.map { team ->
            if (team.isRunning) {
                team.copy(currentLapTimeInMillis = team.currentLapTimeInMillis + elapsed)
            } else {
                team
            }
        }
        _teams.value = updatedTeams
        Log.d("TeamRepositoryImpl", "Timers atualizados. A primeira equipe correndo tem o tempo: ${updatedTeams.find { it.isRunning }?.currentLapTimeInMillis}")

    }

    override suspend fun recordLap(teamId: Long) {
        val team = getTeamById(teamId) ?: return

        val teamStateForSheet = team.copy(
            laps = team.laps + 1,
            totalTimeInMillis = team.totalTimeInMillis + team.currentLapTimeInMillis,
            isRunning = true
        )
        val newTeamStateForApp = teamStateForSheet.copy(currentLapTimeInMillis = 0L)

        val updatedTeamsForApp = _teams.value.map {
            if (it.id == teamId) newTeamStateForApp else it
        }

        localDataSource.saveTeams(updatedTeamsForApp)
        _teams.value = updatedTeamsForApp
        remoteDataSource.syncTeam(teamStateForSheet)
    }

    override suspend fun startAllTimers() {
        val teamsToUpdate = _teams.value.filter { !it.isFinished && !it.isRunning }
        if (teamsToUpdate.isEmpty()) return

        val updatedTeams = _teams.value.map {
            if (!it.isFinished) it.copy(isRunning = true) else it
        }
        localDataSource.saveTeams(updatedTeams)
        _teams.value = updatedTeams
        teamsToUpdate.forEach { remoteDataSource.syncTeam(it.copy(isRunning = true)) }
    }

    override suspend fun stopAllTimers() {
        val teamsToUpdate = _teams.value.filter { it.isRunning }
        if (teamsToUpdate.isEmpty()) return

        val updatedTeams = _teams.value.map { it.copy(isRunning = false) }
        localDataSource.saveTeams(updatedTeams)
        _teams.value = updatedTeams
        teamsToUpdate.forEach { remoteDataSource.syncTeam(it.copy(isRunning = false)) }
    }

    companion object {
        @Volatile
        private var INSTANCE: TeamRepository? = null

        fun getInstance(context: Context): TeamRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = TeamRepositoryImpl(
                    TeamLocalDataSource(context.applicationContext),
                    GoogleSheetDataSource()
                )
                INSTANCE = instance
                instance
            }
        }
    }

}
