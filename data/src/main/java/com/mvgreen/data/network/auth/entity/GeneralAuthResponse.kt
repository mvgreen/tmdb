package com.mvgreen.data.network.auth.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneralAuthResponse(
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "request_token")
    val requestToken: String?,
    @Json(name = "session_id")
    val sessionId: String?,
    @Json(name = "status_message")
    val statusMessage: String?,
    @Json(name = "status_code")
    val statusCode: Int?
)