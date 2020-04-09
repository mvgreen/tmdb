package com.mvgreen.data.network.auth

import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.auth.entity.CreateSessionRequest
import com.mvgreen.data.network.auth.entity.ValidateTokenRequest
import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: TMDbApi
) : AuthRepository {

    override fun login(email: String, password: String): Single<String> {
        return api
            .getRequestToken()
            .flatMap { result ->
                val token = result.requestToken ?: throw UnexpectedResponseException()
                return@flatMap api.validateRequestToken(
                    ValidateTokenRequest(
                        email,
                        password,
                        token
                    )
                )
            }
            .flatMap { result ->
                val token = result.requestToken ?: throw UnexpectedResponseException()
                return@flatMap api.createSession(CreateSessionRequest(token))
            }
            .map { result -> result.sessionId ?: throw UnexpectedResponseException() }
    }

    override fun loadProfile(sessionToken: String): Single<ProfileData> {
        return api
            .getAccountData(sessionToken)
            .map { response ->
                ProfileData(response.avatar?.gravatar?.hash)
            }
    }

}