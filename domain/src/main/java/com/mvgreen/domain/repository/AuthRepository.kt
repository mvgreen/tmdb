package com.mvgreen.domain.repository

import io.reactivex.Single

interface AuthRepository {

    fun login(email: String, password: String) : Single<String>

}