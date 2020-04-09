package com.mvgreen.data.network.auth.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetProfileResponse(
    @Json(name = "avatar")
    val avatar: AvatarResponse?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "iso_639_1")
    val language: String?,
    @Json(name = "iso_3166_1")
    val country: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "include_adult")
    val includeAdult: Boolean?,
    @Json(name = "username")
    val username: String?,
    // Error fields
    @Json(name = "status_message")
    val statusMessage: String?,
    @Json(name = "status_code")
    val statusCode: Int?
)

@JsonClass(generateAdapter = true)
data class AvatarResponse(
    @Json(name = "gravatar")
    val gravatar: GravatarResponse?
)

@JsonClass(generateAdapter = true)
data class GravatarResponse(
    @Json(name = "hash")
    val hash: String?
)