package com.mvgreen.data.network.image.api

import com.mvgreen.data.network.factory.NetworkConstants
import com.mvgreen.data.network.image.entity.ImageConfigurationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageConfigurationApi {

    @GET("configuration")
    fun getConfiguration(
        @Query("api_key")
        apiKey: String = NetworkConstants.API_KEY
    ) : ImageConfigurationResponse

}