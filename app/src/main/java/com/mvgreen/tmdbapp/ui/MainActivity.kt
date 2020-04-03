package com.mvgreen.tmdbapp.ui

import android.os.Bundle
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.activity.AppActivity

class MainActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val router = DI.appComponent.router()
        if (savedInstanceState == null) {
//            updateStatusBar(R.color.silver)
//            router.newRootChain(MainScreen, SplashScreen)
        }
    }
}