package com.mvgreen.domain.usecase

import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.ProfileData

interface ProfileUseCase {

    fun initAvatarLoader(imageLoader: ImageLoader)

    fun getProfileData(): ProfileData

    fun logout()

}