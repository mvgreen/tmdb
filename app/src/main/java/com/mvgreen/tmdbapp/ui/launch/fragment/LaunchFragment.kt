package com.mvgreen.tmdbapp.ui.launch.fragment

import android.os.Bundle
import android.util.Log
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.AuthScreen
import com.mvgreen.tmdbapp.ui.cicerone.MainScreen
import com.mvgreen.tmdbapp.ui.launch.viewmodel.LaunchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_launch.*
import ru.terrakok.cicerone.Router

class LaunchFragment : BaseFragment(R.layout.fragment_launch) {

    companion object {
        const val TAG = "LaunchFragment"
    }

    private lateinit var viewModel: LaunchViewModel
    private lateinit var userDataStorage: UserDataStorage
    private lateinit var mainRouter: Router
    private lateinit var loadingDelegate : LoadingStateDelegate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFragment()
        loadConfig()
    }

    private fun setupFragment() {
        loadingDelegate = LoadingStateDelegate(loadingView = launch_loading, stubView = launch_error)

        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.launchViewModel()
        })
        mainRouter = DI.appComponent.router()
        userDataStorage = DI.appComponent.userDataStorage()

        button_retry.setOnClickListener {
            loadConfig()
        }
    }

    private fun loadConfig() {
        loadingDelegate.showLoading()
        viewModel
            .onLoadConfig()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (userDataStorage.hasUserData()) {
                        mainRouter.newRootScreen(MainScreen)
                    } else {
                        mainRouter.newRootScreen(AuthScreen)
                    }
                },
                { e ->
                    Log.e(TAG, e.message, e)
                    loadingDelegate.showStub()
                }
            )
            .disposeOnDestroy()
    }

}