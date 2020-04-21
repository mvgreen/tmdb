package com.mvgreen.tmdbapp.ui.launch.fragment

import android.os.Bundle
import android.util.Log
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.event.Event
import com.mvgreen.tmdbapp.ui.base.event.LoadCompletedEvent
import com.mvgreen.tmdbapp.ui.base.event.LoadErrorEvent
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.AuthScreen
import com.mvgreen.tmdbapp.ui.cicerone.MainScreen
import com.mvgreen.tmdbapp.ui.launch.viewmodel.LaunchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.observe
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
import kotlinx.android.synthetic.main.fragment_launch.*
import ru.terrakok.cicerone.Router

class LaunchFragment : BaseFragment(R.layout.fragment_launch) {

    companion object {
        const val TAG = "LaunchFragment"
    }

    private lateinit var viewModel: LaunchViewModel
    private lateinit var mainRouter: Router
    private lateinit var loadingDelegate: LoadingStateDelegate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRouter()
        setupView()
        setupViewModel()

        loadConfig()
    }

    private fun setupRouter() {
        mainRouter = DI.appComponent.router()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.launchViewModel()
        })
        observe(viewModel.events, ::onEvent)
    }

    private fun setupView() {
        loadingDelegate =
            LoadingStateDelegate(loadingView = launch_loading, stubView = launch_error)

        button_retry.setOnClickListener {
            loadConfig()
        }
    }

    private fun loadConfig() {
        loadingDelegate.showLoading()
        viewModel.onLoadConfig()
    }

    private fun onEvent(event: Event) {
        when(event) {
            is LoadErrorEvent -> {
                loadingDelegate.showStub()
            }

            is LoadCompletedEvent -> {
                if (viewModel.hasUserData()) {
                    mainRouter.newRootScreen(MainScreen)
                } else {
                    mainRouter.newRootScreen(AuthScreen)
                }
            }
            else -> Log.e(TAG, "Unexpected event $event")
        }
    }

}