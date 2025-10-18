package com.example.laptrack.app.data.source.local

import android.content.Context
import android.util.Log
import com.example.laptrack.app.domain.model.Team
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TeamLocalDataSource(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("LapTrackApp", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val TAG = "TeamLocalDataSource"

    companion object {
        private const val TEAMS_KEY = "teams_list"
    }

    fun getTeams(): List<Team> {
        val json = sharedPreferences.getString(TEAMS_KEY, null)
        Log.d(TAG, "getTeams: JSON encontrado no SharedPreferences: ${!json.isNullOrEmpty()}")
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        val type = object : TypeToken<List<Team>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveTeams(teams: List<Team>) {
        val json = gson.toJson(teams)
        Log.d(TAG, "saveTeams: Salvando JSON no SharedPreferences: $json")
        sharedPreferences.edit().putString(TEAMS_KEY, json).apply()
    }
}
