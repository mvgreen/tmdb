package com.mvgreen.tmdbapp.ui

import android.os.Bundle
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.activity.AppActivity
import com.mvgreen.tmdbapp.ui.cicerone.AuthScreen
import com.mvgreen.tmdbapp.ui.cicerone.MainScreen

class MainActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0.0f
        val router = DI.appComponent.router()
        if (savedInstanceState == null) {
            if (DI.appComponent.userDataStorage().hasUserData()) {
                router.newRootScreen(MainScreen)
            } else {
                router.newRootScreen(AuthScreen)
            }
        }
    }

}