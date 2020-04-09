package com.mvgreen.data.usecase

import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.CredentialsStorage
import com.mvgreen.domain.usecase.AuthUseCase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val credentialsStorage: CredentialsStorage
) : AuthUseCase {

    override fun login(email: String, password: String): Completable {
        return repository
            .login(email, password)
            .map { token ->
                credentialsStorage.saveAuthData(token, email, password)
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }

}