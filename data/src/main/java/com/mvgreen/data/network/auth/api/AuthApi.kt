package com.mvgreen.data.network.auth.api

import com.mvgreen.data.network.auth.entity.CreateSessionRequest
import com.mvgreen.data.network.auth.entity.GeneralAuthResponse
import com.mvgreen.data.network.auth.entity.GetProfileResponse
import com.mvgreen.data.network.auth.entity.ValidateTokenRequest
import com.mvgreen.data.network.factory.NetworkConstants.API_KEY
import com.mvgreen.data.network.factory.NetworkConstants.SESSION_ID
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @GET("authentication/token/new")
    fun getRequestToken(
        @Query("api_key") apiKey: String = API_KEY
    ): Single<GeneralAuthResponse>

    @POST("authentication/token/validate_with_login")
    fun validateRequestToken(
        @Body requestBody: ValidateTokenRequest,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<GeneralAuthResponse>

    @POST("authentication/session/new")
    fun createSession(
        @Body requestBody: CreateSessionRequest,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<GeneralAuthResponse>

    @GET("account")
    fun getAccountData(
        @Query(SESSION_ID) sessionId : String,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<GetProfileResponse>

}