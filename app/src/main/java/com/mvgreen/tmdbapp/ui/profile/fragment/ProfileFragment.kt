package com.mvgreen.tmdbapp.ui.profile.fragment

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.mvgreen.data.exception.StorageException
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.AuthScreen
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.terrakok.cicerone.Router

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private lateinit var profileUseCase: ProfileUseCase
    private lateinit var loadImageUseCase: LoadImageUseCase
    private lateinit var mainRouter: Router

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileUseCase = DI.appComponent.profileUseCase()
        loadImageUseCase = DI.appComponent.loadImageUseCase()
        mainRouter = DI.appComponent.router()

        setupView()
        loadImage()
    }

    private fun setupView() {
        try {
            val profile = profileUseCase.getProfileData()
            name.text = profile.name
            login.text = profile.login
        } catch (e: StorageException) {
            profileUseCase.logout()
            mainRouter.newRootScreen(AuthScreen)
        }

        btn_logout.setOnClickListener {
            profileUseCase.logout()
            mainRouter.newRootScreen(AuthScreen)
        }
    }

    private fun loadImage() {
        try {
            val imageLoader = ImageLoaderImpl(
                avatar,
                R.drawable.ic_profile_stub,
                true) {
                Snackbar
                    .make(
                        requireView(),
                        getString(R.string.glide_error_message),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
            loadImageUseCase.initAvatarLoader(imageLoader)
            imageLoader.loadImage()
        } catch (e: StorageException) {
            profileUseCase.logout()
            mainRouter.newRootScreen(AuthScreen)
        }
    }

}