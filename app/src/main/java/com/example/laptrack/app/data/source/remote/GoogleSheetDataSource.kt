package com.example.laptrack.app.data.source.remote

import android.util.Log
import com.example.laptrack.app.domain.model.Team
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GoogleSheetDataSource {

    // --- IMPORTANTE ---
    // Cole a URL do seu App da Web do Google Apps Script aqui.
    private val scriptUrl = "https://script.google.com/macros/s/AKfycbzPQRCTDpJ9uykC6Yu71l4R-frTnUAN3p8n5bfYqLuDv9HtpHjxX-mKEKkMd_6DGyYY/execsave"
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun syncTeam(team: Team) {
        // Verifica se a URL foi alterada
        if (scriptUrl == "https://script.google.com/macros/s/AKfycbzPQRCTDpJ9uykC6Yu71l4R-frTnUAN3p8n5bfYqLuDv9HtpHjpX-mKEKkMd_6DGyYY/exec") {
            Log.e("GoogleSheetDataSource", "URL do Google Apps Script não foi definida.")
            return
        }

        try {
            Log.d("GoogleSheetDataSource", "Enviando ${team.name} para a planilha...")
            val response: HttpResponse = client.post(scriptUrl) {
                contentType(ContentType.Application.Json)
                setBody(team)
            }
            Log.d("GoogleSheetDataSource", "Resposta da planilha: ${response.status} - ${response.bodyAsText()}")
        } catch (e: Exception) {
            Log.e("GoogleSheetDataSource", "Erro ao atualizar a planilha: ${e.message}")
        }
    }
}

