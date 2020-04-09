package com.mvgreen.tmdbapp.ui.profile.fragment

import android.os.Bundle
import android.util.Log
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private lateinit var profileUseCase: ProfileUseCase

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileUseCase = DI.appComponent.profileUseCase()

        val imageLoader = ImageLoaderImpl(avatar)
        profileUseCase.initAvatarLoader(imageLoader)
        imageLoader.loadImage()

        val profile = profileUseCase.getProfileData()
        name.text = profile.name
        login.text = profile.login
    }

}