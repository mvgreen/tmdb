package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.ProfileData
import io.reactivex.Single

interface AuthRepository {

    fun login(login: String, password: String) : Single<String>

    fun loadProfile(sessionToken: String): Single<ProfileData>

}