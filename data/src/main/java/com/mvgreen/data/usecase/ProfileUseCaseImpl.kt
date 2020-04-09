package com.mvgreen.data.usecase

import com.mvgreen.data.network.factory.ServerUrls
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.ProfileUseCase
import io.reactivex.Single
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val userDataStorage: UserDataStorage
) : ProfileUseCase {

    // TODO обработка случая отсутствия данных
    override fun initAvatarLoader(imageLoader: ImageLoader) {
        val profileData = userDataStorage.getProfileData()

        imageLoader.url = ServerUrls.Gravatar.url + profileData.avatarHash
        imageLoader.sizeParam = ServerUrls.Gravatar.sizeModifier
    }

    override fun getProfileData() : ProfileData {
        return userDataStorage.getProfileData()
    }
}