package com.multiplatform.kanoonify.news.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Thin HTTP client wrapping a NewsAPI-compatible upstream.
 *
 *  - Stateless except for the [HttpClient] which is reused for the lifetime
 *    of the instance.
 *  - Endpoints are sane defaults (GNews-compatible); swap [baseUrl] /
 *    [apiKey] to point at any provider sharing the same envelope.
 *
 * The repository — not this class — owns retry / cache / fallback logic.
 */
class NewsApiService(
    private val baseUrl: String = DEFAULT_BASE_URL,
    private val apiKey: String = "",
    private val httpClient: HttpClient = defaultClient()
) {

    suspend fun topHeadlines(
        category: String? = null,
        country: String = DEFAULT_COUNTRY,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsApiResponse = httpClient.get("$baseUrl/top-headlines") {
        if (apiKey.isNotBlank()) parameter("apikey", apiKey)
        parameter("country", country)
        parameter("max", pageSize)
        if (!category.isNullOrBlank() && category != "latest") {
            parameter("topic", category)
        }
    }.body()

    suspend fun search(
        query: String,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsApiResponse = httpClient.get("$baseUrl/search") {
        if (apiKey.isNotBlank()) parameter("apikey", apiKey)
        parameter("q", query)
        parameter("max", pageSize)
    }.body()

    companion object {
        const val DEFAULT_BASE_URL  = "https://gnews.io/api/v4"
        const val DEFAULT_COUNTRY   = "in"
        const val DEFAULT_PAGE_SIZE = 20

        fun defaultClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
        }
    }
}

