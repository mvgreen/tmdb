package com.mvgreen.data.network.api

import com.mvgreen.data.network.entity.auth.CreateSessionRequest
import com.mvgreen.data.network.entity.auth.GeneralAuthResponse
import com.mvgreen.data.network.entity.auth.ValidateTokenRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TMDbApi {

    companion object {
        const val API_KEY = "24721d9f11f943e33acba3cc602e8789"
    }

    @GET("authentication/token/new/")
    fun getRequestToken(
        @Query("api_key") apiKey: String = API_KEY
    ): Single<GeneralAuthResponse>

    @POST("authentication/token/validate_with_login/")
    fun validateRequestToken(
        @Query("api_key") apiKey: String = API_KEY,
        @Body requestBody: ValidateTokenRequest
    ): Single<GeneralAuthResponse>

    @POST("authentication/session/new/")
    fun createSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body requestBody: CreateSessionRequest
    ): Single<GeneralAuthResponse>

}