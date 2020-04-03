package com.mvgreen.tmdbapp.ui

import android.app.Application
import com.mvgreen.tmdbapp.internal.di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        DI.init(this)
    }
}