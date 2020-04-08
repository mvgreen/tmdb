package com.mvgreen.domain.repository

interface TokenStorage {

    fun saveAuthData(sessionToken: String, email: String, password: String)

    fun getSessionToken(): String?

    fun getCredentials(): Pair<String?, String?>

}