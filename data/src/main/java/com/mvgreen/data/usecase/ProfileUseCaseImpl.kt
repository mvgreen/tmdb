package com.mvgreen.data.usecase

import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.ProfileUseCase
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val userDataStorage: UserDataStorage
) : ProfileUseCase {

    override fun getProfileData(): ProfileData {
        return userDataStorage.getProfileDataOrDie()
    }

    override fun logout() {
        userDataStorage.clearData()
    }
}