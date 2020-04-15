package com.mvgreen.data.network.factory

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

abstract class ApiFactory(
    private val serverUrl: NetworkConstants.ServerUrl,
    private val httpClient: OkHttpClient,
    private vararg val converterFactories: Converter.Factory
) {

    fun <T> create(clazz: Class<T>): T {
        val builder = Retrofit.Builder()
            .baseUrl(serverUrl.url)
            .client(httpClient)

        for (factory in converterFactories) {
            builder.addConverterFactory(factory)
        }

        return builder
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(clazz)
    }

}