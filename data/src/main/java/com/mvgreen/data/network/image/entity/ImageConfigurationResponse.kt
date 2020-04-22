package com.mvgreen.data.network.image.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageConfigurationResponse(
    @Json(name = "images")
    val images: ImagesResponse?
)

@JsonClass(generateAdapter = true)
data class ImagesResponse(
    @Json(name = "secure_base_url")
    val secureBaseUrl: String?,
    @Json(name = "logo_sizes")
    val logoSizes: List<String>?
)