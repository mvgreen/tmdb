package com.mvgreen.data.usecase

import com.mvgreen.data.network.factory.ServerUrls
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.CredentialsStorage
import com.mvgreen.domain.usecase.ProfileUseCase
import io.reactivex.Single
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val credentialsStorage: CredentialsStorage,
    private val authRepository: AuthRepository
) : ProfileUseCase {

    // TODO обработка случая отсутствия данных
    override fun getAvatarLoader(): Single<ImageLoader> {
        val sessionToken = credentialsStorage.getSessionToken() ?: throw IllegalStateException()
        return authRepository
            .loadProfile(sessionToken)
            .flatMap { profile ->
                Single.just(ImageLoader(ServerUrls.Gravatar.url + profile.avatarHash))
            }
    }

}