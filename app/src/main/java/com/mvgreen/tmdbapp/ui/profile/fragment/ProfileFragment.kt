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
import com.mvgreen.tmdbapp.ui.cicerone.FilmsBranchScreen
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import com.mvgreen.tmdbapp.ui.profile.viewmodel.ProfileViewModel
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.terrakok.cicerone.Router

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var filmsRouter: SelfRestoringRouter
    private lateinit var mainRouter: Router

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainRouter = DI.appComponent.router()
        filmsRouter = DI.filmsTabComponent.router()

        setupViewModel()
        setupView()
        loadImage()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.profileViewModel()
        })
    }

    private fun setupView() {
        try {
            val profile = viewModel.getProfileData()
            name.text = profile.name
            login.text = profile.login
        } catch (e: StorageException) {
            viewModel.onExit()
            mainRouter.newRootScreen(AuthScreen)
        }

        btn_logout.setOnClickListener {
            viewModel.onExit()
            filmsRouter.reset()
            mainRouter.newRootScreen(AuthScreen)
        }
    }

    private fun loadImage() {
        try {
            val imageLoader = ImageLoaderImpl(
                avatar,
                R.drawable.ic_profile_stub,
                true
            ) {
                Snackbar
                    .make(
                        requireView(),
                        getString(R.string.glide_error_message),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
            viewModel.onLoadAvatar(imageLoader)
        } catch (e: StorageException) {
            viewModel.onExit()
            mainRouter.newRootScreen(AuthScreen)
        }
    }

}