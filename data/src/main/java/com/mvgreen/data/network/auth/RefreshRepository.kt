package com.mvgreen.data.network.auth

import com.mvgreen.data.exception.NetworkException
import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.auth.api.ApiHolder
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.auth.entity.CreateSessionRequest
import com.mvgreen.data.network.auth.entity.ValidateTokenRequest
import com.mvgreen.domain.entity.ProfileData
import javax.inject.Inject

class RefreshRepository @Inject constructor(
    private val apiHolder: ApiHolder
) {

    private val api : TMDbApi by lazy { apiHolder.api }

    fun loginSync(login: String, password: String): String {
        try {
            return api
                .getRequestToken()
                .flatMap { result ->
                    val token = result.requestToken ?: throw UnexpectedResponseException()
                    return@flatMap api.validateRequestToken(
                        ValidateTokenRequest(
                            login,
                            password,
                            token
                        )
                    )
                }
                .flatMap { result ->
                    val token = result.requestToken ?: throw UnexpectedResponseException()
                    return@flatMap api.createSession(CreateSessionRequest(token))
                }
                .map { result ->
                    result.sessionId ?: throw UnexpectedResponseException()
                }
                .blockingGet()
        } catch (e: Throwable) {
            if (e !is NetworkException) {
                if (e.cause == null || e.cause !is NetworkException) {
                    throw UnexpectedResponseException(e)
                } else throw e.cause as NetworkException
            } else throw e
        }

    }

    fun loadProfileSync(sessionToken: String): ProfileData {
        try {
            return api
                .getAccountData(sessionToken)
                .map { response ->
                    ProfileData(
                        response.avatar?.gravatar?.hash ?: throw UnexpectedResponseException(),
                        response.name ?: throw UnexpectedResponseException(),
                        response.username ?: throw UnexpectedResponseException()
                    )
                }
                .blockingGet()
        } catch (e: Throwable) {
            if (e !is NetworkException) {
                if (e.cause == null || e.cause !is NetworkException) {
                    throw UnexpectedResponseException(e)
                } else throw e.cause as NetworkException
            } else throw e
        }
    }

}