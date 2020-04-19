package com.mvgreen.tmdbapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.activity.AppActivity
import com.mvgreen.tmdbapp.ui.cicerone.AuthScreen
import com.mvgreen.tmdbapp.ui.cicerone.MainScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity constructor() : AppActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    // TODO нормальный диспоз
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0.0f
        val router = DI.appComponent.router()
        val loadImageUseCase = DI.appComponent.loadImageUseCase()
        if (savedInstanceState == null) {
            loadImageUseCase
                .downloadConfiguration()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (DI.appComponent.userDataStorage().hasUserData()) {
                            router.newRootScreen(MainScreen)
                        } else {
                            router.newRootScreen(AuthScreen)
                        }
                    },
                    { e ->
                        // TODO обработка вылетов
                        Log.e(TAG, e.message, e)
                        Snackbar
                            .make(
                                container,
                                getString(R.string.error_download_config),
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                )
        }
    }

}