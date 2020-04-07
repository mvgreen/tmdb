package com.mvgreen.data.network.auth

import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.auth.entity.CreateSessionRequest
import com.mvgreen.data.network.auth.entity.ValidateTokenRequest
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: TMDbApi
) {

    fun login(email: String, password: String): Single<String> {
        return api
            .getRequestToken()
            .flatMap { result ->
                val token = result.requestToken ?: throw UnexpectedResponseException()
                return@flatMap api.validateRequestToken(ValidateTokenRequest(email, password, token))
            }
            .flatMap { result ->
                val token = result.requestToken ?: throw UnexpectedResponseException()
                return@flatMap api.createSession(CreateSessionRequest(token))
            }
            .map { result -> result.sessionId ?: throw UnexpectedResponseException() }
    }

}