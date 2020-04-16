package com.mvgreen.data.network.factory

import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Inject

class TMDbGuestApiFactory @Inject constructor(
    httpErrorInterceptor: HttpErrorInterceptor,
    vararg converters: Converter.Factory
) : ApiFactory(
    NetworkConstants.TMDb(),
    OkHttpClient
        .Builder()
        .addInterceptor(httpErrorInterceptor)
        .build(),
    *converters
)