package com.example.laptrack.app.domain.repository

import com.example.laptrack.app.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository{
    fun getTeams(): Flow<List<Team>>
    suspend fun addTeam(team: String)
    suspend fun updateTeam(team: Team)
    suspend fun getTeamById(teamId: Long): Team?
    suspend fun updateTeamStateInMemory(team: Team)
    suspend fun recordLap(teamId: Long)
    suspend fun updateAllRunningTimers(elapsed: Long)

    suspend fun startAllTimers()
    suspend fun stopAllTimers()

}