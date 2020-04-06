package com.mvgreen.tmdbapp.ui.cicerone

import com.mvgreen.tmdbapp.ui.fragment.AuthFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AuthScreen: SupportAppScreen() {
    override fun getFragment() = AuthFragment()
}