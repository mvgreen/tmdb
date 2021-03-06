package com.mvgreen.data.usecase

import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.SearchUseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val userDataStorage: UserDataStorage
) : AuthUseCase {

    override fun login(email: String, password: String): Completable {
        return repository
            .login(email, password)
            .flatMapCompletable { token ->
                userDataStorage.saveAuthData(token, email, password)
                loadProfileData(token).ignoreElement()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun hasUserData(): Boolean {
        return userDataStorage.hasUserData()
    }

    private fun loadProfileData(token: String): Single<Unit> {
        return repository.loadProfile(token)
            .map { profile ->
                userDataStorage.saveProfileData(profile)
            }
    }

}