package com.mvgreen.tmdbapp.ui.cicerone

import com.mvgreen.tmdbapp.ui.auth.fragment.AuthFragment
import com.mvgreen.tmdbapp.ui.rootscreen.fragment.MainFragmentStub
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AuthScreen: SupportAppScreen() {
    override fun getFragment() =
        AuthFragment()
}

object MainScreen: SupportAppScreen() {
    override fun getFragment() =
        MainFragmentStub()
}