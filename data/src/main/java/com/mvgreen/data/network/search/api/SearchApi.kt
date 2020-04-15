package com.mvgreen.data.network.search.api

import com.mvgreen.data.network.factory.NetworkConstants
import com.mvgreen.data.network.search.entity.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/movie")
    fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String = "ru-RU",
        @Query("api_key") apiKey: String = NetworkConstants.API_KEY
    ): Single<SearchResponse>

}