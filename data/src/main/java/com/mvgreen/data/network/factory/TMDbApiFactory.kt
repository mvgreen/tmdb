package com.mvgreen.data.network.factory

import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Inject

class TMDbApiFactory @Inject constructor(
    httpErrorInterceptor: HttpErrorInterceptor,
    authenticator: Authenticator,
    vararg converters: Converter.Factory
) : ApiFactory(
    ServerUrls.TMDb(),
    OkHttpClient
        .Builder()
        .addInterceptor(httpErrorInterceptor)
        .authenticator(authenticator)
        .build(),
    *converters
)