package com.mvgreen.data.network.factory

import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Inject

class TMDbApiFactory @Inject constructor(
    vararg converters: Converter.Factory
) : ApiFactory(
    ServerUrls.TMDb(),
    OkHttpClient
        .Builder()
        .build(),
    *converters
)