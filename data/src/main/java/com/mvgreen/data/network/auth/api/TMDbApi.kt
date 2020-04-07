package com.mvgreen.data.network.auth.api

import com.mvgreen.data.network.auth.entity.CreateSessionRequest
import com.mvgreen.data.network.auth.entity.GeneralAuthResponse
import com.mvgreen.data.network.auth.entity.ValidateTokenRequest
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TMDbApi {

    companion object {
        const val API_KEY = "24721d9f11f943e33acba3cc602e8789"
    }

    @GET("authentication/token/new")
    fun getRequestToken(
        @Query("api_key") apiKey: String = API_KEY
    ): Call<GeneralAuthResponse>

    @POST("authentication/token/validate_with_login")
    fun validateRequestToken(
        @Body requestBody: ValidateTokenRequest,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<GeneralAuthResponse>

    @POST("authentication/session/new")
    fun createSession(
        @Body requestBody: CreateSessionRequest,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<GeneralAuthResponse>

}