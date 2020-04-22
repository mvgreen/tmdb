package com.mvgreen.domain.usecase

import com.mvgreen.domain.entity.ProfileData

interface ProfileUseCase {

    fun getProfileData(): ProfileData

    fun logout()

}