package com.multiplatform.kanoonify.data.remote

class OpenAIService {

    suspend fun getLegalAdvice(query: String, law: String): String {
        return try {
            // TEMP mock (replace later with real API)
            "Based on Indian law: $law\n\nAdvice: You may be fined. Always wear a helmet."
        } catch (e: Exception) {
            "Something went wrong"
        }
    }
}