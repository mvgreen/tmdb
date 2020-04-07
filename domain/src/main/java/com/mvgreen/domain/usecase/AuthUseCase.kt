package com.mvgreen.domain.usecase

import io.reactivex.Completable

interface AuthUseCase {

    fun login(email: String, password: String): Completable

}