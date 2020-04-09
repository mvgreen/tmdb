package com.mvgreen.data.usecase

import com.mvgreen.data.network.factory.ServerUrls
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.CredentialsStorage
import com.mvgreen.domain.usecase.ProfileUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialsStorage: CredentialsStorage
) : ProfileUseCase {

    // TODO обработка случая отсутствия данных
    override fun getAvatarLoader(imageLoader: ImageLoader): Single<ImageLoader> {
        val sessionToken = credentialsStorage.getSessionToken() ?: throw IllegalStateException()
        return authRepository
            .loadProfile(sessionToken)
            .subscribeOn(Schedulers.io())
            .map { profile ->
                imageLoader.url = ServerUrls.Gravatar.url + profile.avatarHash
                imageLoader.sizeParam = ServerUrls.Gravatar.sizeModifier
                imageLoader
            }
    }

}