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
        return Single.fromCallable {
            val requestToken = api
                .getRequestToken()
                .execute()
                .body()
                ?.requestToken
                ?: throw UnexpectedResponseException()

            api.validateRequestToken(ValidateTokenRequest(email, password, requestToken)).execute()

            return@fromCallable api
                .createSession(CreateSessionRequest(requestToken))
                .execute()
                .body()
                ?.sessionId
                ?: throw UnexpectedResponseException()
        }
    }

}