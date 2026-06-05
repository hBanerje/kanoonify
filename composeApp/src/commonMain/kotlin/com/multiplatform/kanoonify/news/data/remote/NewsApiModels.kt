package com.multiplatform.kanoonify.news.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wire DTOs for a NewsAPI-compatible endpoint. Keep these strictly serializable
 * with no logic — domain mapping happens in [com.multiplatform.kanoonify.news.data.mapper].
 *
 * The shape mirrors the widely-used GNews / NewsAPI JSON format so the same
 * client can be pointed at any compatible provider by swapping the base URL.
 */
@Serializable
data class NewsApiResponse(
    @SerialName("status")       val status: String? = null,
    @SerialName("totalResults") val totalResults: Int? = null,
    @SerialName("articles")     val articles: List<NewsApiArticleDto> = emptyList(),
    @SerialName("message")      val message: String? = null
)

@Serializable
data class NewsApiArticleDto(
    @SerialName("source")      val source: NewsApiSourceDto? = null,
    @SerialName("author")      val author: String? = null,
    @SerialName("title")       val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("url")         val url: String? = null,
    @SerialName("urlToImage")  val urlToImage: String? = null,
    @SerialName("image")       val image: String? = null,
    @SerialName("publishedAt") val publishedAt: String? = null,
    @SerialName("content")     val content: String? = null
)

@Serializable
data class NewsApiSourceDto(
    @SerialName("id")   val id: String? = null,
    @SerialName("name") val name: String? = null
)

