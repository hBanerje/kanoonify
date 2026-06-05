package com.multiplatform.kanoonify.news.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NewsApiService(
    private val baseUrl: String = DEFAULT_BASE_URL,
    private val apiKey: String = "",
    private val httpClient: HttpClient = defaultClient()
) {

    suspend fun topHeadlines(
        category: String? = null,
        country: String? = DEFAULT_COUNTRY,
        query: String? = null,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsApiResponse = httpClient.get("$baseUrl/top-headlines") {
        header(API_KEY_HEADER, apiKey)
        if (!country.isNullOrBlank()) parameter("country", country)
        if (!category.isNullOrBlank()) parameter("category", category)
        if (!query.isNullOrBlank())    parameter("q", query)
        parameter("pageSize", pageSize.coerceIn(1, 100))
    }.body()

    suspend fun everything(
        query: String,
        language: String = DEFAULT_LANGUAGE,
        sortBy: String = DEFAULT_SORT,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsApiResponse = httpClient.get("$baseUrl/everything") {
        header(API_KEY_HEADER, apiKey)
        parameter("q", query)
        parameter("language", language)
        parameter("sortBy", sortBy)
        parameter("pageSize", pageSize.coerceIn(1, 100))
    }.body()

    suspend fun search(
        query: String,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsApiResponse = everything(query = query, pageSize = pageSize)

    companion object {
        const val DEFAULT_BASE_URL  = "https://newsapi.org/v2"
        const val DEFAULT_COUNTRY   = "in"
        const val DEFAULT_LANGUAGE  = "en"
        const val DEFAULT_SORT      = "publishedAt"
        const val DEFAULT_PAGE_SIZE = 30

        private const val API_KEY_HEADER = "X-Api-Key"

        private const val USER_AGENT = "Kanoonify/1.0 (Kotlin Multiplatform)"

        fun defaultClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
            defaultRequest {
                header("User-Agent", USER_AGENT)
                header("Accept", "application/json")
            }
        }
    }
}
