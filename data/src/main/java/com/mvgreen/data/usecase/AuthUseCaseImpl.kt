package com.mvgreen.data.usecase

import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.AuthUseCase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val userDataStorage: UserDataStorage
) : AuthUseCase {

    override fun login(email: String, password: String): Completable {
        return repository
            .login(email, password)
            .flatMap { token ->
                userDataStorage.saveAuthData(token, email, password)
                repository.loadProfile(token)
            }
            .map { profile ->
                userDataStorage.saveProfileData(profile)
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }

}