package com.mvgreen.data.usecase

import com.mvgreen.data.network.factory.ServerUrls
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.ProfileUseCase
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val userDataStorage: UserDataStorage
) : ProfileUseCase {

    override fun initAvatarLoader(imageLoader: ImageLoader) {
        val profileData = userDataStorage.getProfileDataOrDie()

        imageLoader.url = ServerUrls.Gravatar.url + profileData.avatarHash
        imageLoader.sizeParam = ServerUrls.Gravatar.sizeModifier
    }

    override fun getProfileData() : ProfileData {
        return userDataStorage.getProfileDataOrDie()
    }

    override fun logout() {
        userDataStorage.clearData()
    }
}