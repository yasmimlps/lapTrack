package com.example.laptrack.app.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.laptrack.R
import com.example.laptrack.app.data.repository.TeamRepositoryImpl
import com.example.laptrack.app.domain.repository.TeamRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class RaceTimerService : Service() {

    private lateinit var repository: TeamRepository
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var lastUpdateTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        repository = TeamRepositoryImpl.getInstance(this)
        Log.d("RaceTimerService", "Serviço criado.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("RaceTimerService", "Serviço iniciado.")
        startForegroundService()
        startTimer()
        return START_STICKY
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) {
            Log.d("RaceTimerService", "O timer já está a correr.")
            return
        }

        lastUpdateTime = System.currentTimeMillis()
        Log.d("RaceTimerService", "Iniciando loop do timer.")

        timerJob = serviceScope.launch {
            while (isActive) {
                delay(1000) // Atualiza a cada segundo
                val currentTime = System.currentTimeMillis()
                val elapsed = currentTime - lastUpdateTime

                // A LÓGICA DE PARAR O SERVIÇO VOLTOU PARA CÁ (MAIS CONFIÁVEL)
                val hasRunningTeams = repository.getTeams().first().any { it.isRunning }

                if (!hasRunningTeams) {
                    Log.d("RaceTimerService", "Nenhuma equipe correndo. Parando o serviço.")
                    stopSelf() // Para o serviço se não houver mais cronómetros ativos
                    break // Sai do loop
                }

                Log.d("RaceTimerService", "Atualizando timers em ${elapsed}ms.")
                repository.updateAllRunningTimers(elapsed)

                lastUpdateTime = currentTime
            }
        }
    }

    private fun startForegroundService() {
        val channelId = "race_timer_channel"
        val channelName = "Cronómetro da Corrida"
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Corrida em Andamento")
            .setContentText("A contagem de voltas está ativa.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        Log.d("RaceTimerService", "Serviço destruído.")
        timerJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
