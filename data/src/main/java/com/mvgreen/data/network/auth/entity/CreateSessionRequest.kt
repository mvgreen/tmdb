package com.mvgreen.data.network.auth.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSessionRequest(
    @Json(name = "request_token")
    val requestToken: String
)