package com.mvgreen.data.network.authenticator

import com.mvgreen.data.exception.CredentialsException
import com.mvgreen.data.network.auth.RefreshRepository
import com.mvgreen.data.network.auth.api.TMDbApi.Companion.SESSION_ID
import com.mvgreen.domain.repository.UserDataStorage
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val refreshRepository: RefreshRepository,
    private val userDataStorage: UserDataStorage
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        /* Синхронизация нужна для предотвращения состояния гонки, когда множество запросов одновременно пытаются обновить токен */
        synchronized(this) {
            val sessionToken = response.request().url().queryParameter(SESSION_ID)
                ?: throw CredentialsException()

            val (login, password) = userDataStorage.getCredentials()
            login ?: throw CredentialsException()
            password ?: throw CredentialsException()

            val storedSessionToken =
                userDataStorage.getSessionToken() ?: throw CredentialsException()

            /* Если провалившийся запрос имеет тот же самый токен, что и текущий юзер, значит пора обновляться */
            /* Иначе токен уже обновлен и берем его для повторного запроса */
            return if (sessionToken == storedSessionToken) {
                val newAccessToken = refreshAndReturnNewAccessToken(login, password)
                buildRequestWithAccessToken(response, newAccessToken)
            } else {
                buildRequestWithAccessToken(response, storedSessionToken)
            }
        }
    }

    private fun refreshAndReturnNewAccessToken(login: String, password: String): String {
        // Удаляем данные из хранилища на случай, если данные для входа сменились,
        // иначе обработка может уйти в рекурсию
        userDataStorage.clearData()

        val newToken = refreshRepository.loginSync(login, password)
        userDataStorage.saveAuthData(newToken, login, password)
        userDataStorage.saveProfileData(refreshRepository.loadProfileSync(newToken))
        return newToken
    }

    private fun buildRequestWithAccessToken(response: Response, sessionToken: String): Request {
        val newUrl = response
            .request()
            .url()
            .newBuilder()
            .removeAllQueryParameters(SESSION_ID)
            .addQueryParameter(SESSION_ID, sessionToken)
            .build()

        return response
            .request()
            .newBuilder()
            .url(newUrl)
            .build()
    }

}