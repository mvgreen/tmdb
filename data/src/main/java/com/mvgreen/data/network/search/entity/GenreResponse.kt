package com.mvgreen.data.network.search.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponse(
    @Json(name = "genres")
    val genres: List<Genre>?,
    @Json(name = "status_message")
    val statusMessage: String?,
    @Json(name = "status_code")
    val statusCode: Int?
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?
)