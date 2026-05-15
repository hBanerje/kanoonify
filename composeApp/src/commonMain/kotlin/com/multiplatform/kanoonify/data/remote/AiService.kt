package com.multiplatform.kanoonify.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.multiplatform.kanoonify.data.remote.dto.GeminiResponse

class AiService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun getLegalAdvice(query: String): String {

        println("AI SERVICE CALLED: $query")

        val apiKey = "AIzaSyAaLIVBdjAXckirsRacMF5rtatIQF6J0V8"

        val requestBody = mapOf(
            "contents" to listOf(
                mapOf(
                    "parts" to listOf(
                        mapOf(
                            "text" to "Explain Indian law simply with fines, sections and user rights for: $query"
                        )
                    )
                )
            )
        )

        return try {

            val responseText: String = client.post(
                "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=$apiKey"
            ) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }.body()

            // ALWAYS log raw response
            println("RAW RESPONSE: $responseText")

            val parsed = Json {
                ignoreUnknownKeys = true
            }.decodeFromString(GeminiResponse.serializer(), responseText)

            val result = parsed.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?.trim()

            result ?: "No AI response found"

        } catch (e: Exception) {

            println(" AI ERROR: ${e.message}")
            e.printStackTrace()

            "Error: ${e.message}"
        }
    }
}