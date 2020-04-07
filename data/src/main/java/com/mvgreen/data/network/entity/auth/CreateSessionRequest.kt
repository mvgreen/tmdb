package com.mvgreen.data.network.entity.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSessionRequest(
    @Json(name = "request_token")
    val requestToken: String
)