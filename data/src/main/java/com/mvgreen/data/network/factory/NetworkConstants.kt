package com.mvgreen.data.network.factory

object NetworkConstants {

    const val API_KEY = "24721d9f11f943e33acba3cc602e8789"
    const val SESSION_ID = "session_id"

    open class ServerUrl(val url: String)

    class TMDb : ServerUrl("https://api.themoviedb.org/3/")

    object Gravatar: ServerUrl("https://secure.gravatar.com/avatar/") {
        const val sizeModifier = "?size="
    }
}