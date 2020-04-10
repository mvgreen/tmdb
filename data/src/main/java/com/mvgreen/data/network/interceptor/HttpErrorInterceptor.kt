package com.mvgreen.data.network.interceptor

import com.mvgreen.data.exception.*
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class HttpErrorInterceptor @Inject constructor(private val moshi: Moshi) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response: Response
        try {
            response = chain.proceed(chain.request())
        } catch (timeout: TimeoutException) {
            throw TimeoutException()
        } catch (e : NetworkException) {
            throw e
        } catch (e: Throwable) {
            throw ConnectionException(e)
        }

        if (response.isSuccessful) {
            return response
        }

        when (response.code()) {
            in 400..499 -> clientError(response.code(), response.body())
            in 500..599 -> throw ServerException()
            else -> throw UnexpectedResponseException()
        }
    }

    private fun clientError(code: Int, body: ResponseBody?): Nothing {
        if (body == null)
            throw UnexpectedResponseException()
        when (code) {
            401 -> throw CredentialsException()
            else -> throw UnexpectedResponseException()
        }
    }

}