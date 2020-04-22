package com.mvgreen.tmdbapp.ui.profile.viewmodel

import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val loadImageUseCase: LoadImageUseCase,
    private val searchUseCase: SearchUseCase
): BaseViewModel() {

    fun getProfileData(): ProfileData {
        return profileUseCase.getProfileData()
    }

    fun onExit() {
        searchUseCase.saveSearchState(null, 0, "")
        profileUseCase.logout()
    }

    fun onLoadAvatar(imageLoader: ImageLoaderImpl) {
        loadImageUseCase.initAvatarLoader(imageLoader)
        imageLoader.loadImage()
    }

}