package com.mvgreen.domain.repository

interface TokenStorage {

    fun saveSessionToken(sessionToken: String)

    fun getSessionToken(): String?

}