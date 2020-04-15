package com.mvgreen.data.network.search.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val results: List<MovieObject>?,
    @Json(name = "total_results")
    val totalResults: Int?,
    @Json(name = "total_pages")
    val totalPages: Int?,

    @Json(name = "status_message")
    val statusMessage: String?,
    @Json(name = "status_code")
    val statusCode: Int?
)

@JsonClass(generateAdapter = true)
data class MovieObject(
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "adult")
    val adult: Boolean?,
    @Json(name = "overview")
    val overview: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "genre_ids")
    val genreIds: List<Int>?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "original_title")
    val originalTitle: String?,
    @Json(name = "original_language")
    val originalLanguage: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "popularity")
    val popularity: Float?,
    @Json(name = "vote_count")
    val voteCount: Int?,
    @Json(name = "video")
    val video: Boolean?,
    @Json(name = "vote_average")
    val voteAverage: Float?,

    // Для запроса деталей фильма
    @Json(name = "runtime")
    val runtime: Int?,
    @Json(name = "status_message")
    val statusMessage: String?,
    @Json(name = "status_code")
    val statusCode: Int?
)