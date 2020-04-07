package com.mvgreen.data.network.factory

object ServerUrls {
    open class ServerUrl(val url: String)

    class TMDb : ServerUrl("https://api.themoviedb.org/3/")
}