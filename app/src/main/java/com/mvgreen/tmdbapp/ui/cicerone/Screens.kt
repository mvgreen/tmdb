package com.mvgreen.tmdbapp.ui.cicerone

import com.mvgreen.tmdbapp.ui.auth.fragment.AuthFragment
import com.mvgreen.tmdbapp.ui.favorites.fragment.FavoritesBranchFragment
import com.mvgreen.tmdbapp.ui.films.fragment.FilmsBranchFragment
import com.mvgreen.tmdbapp.ui.profile.fragment.ProfileBranchFragment
import com.mvgreen.tmdbapp.ui.rootscreen.fragment.RootFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AuthScreen : SupportAppScreen() {
    override fun getFragment() =
        AuthFragment()
}

object MainScreen : SupportAppScreen() {
    override fun getFragment() = RootFragment()
}

object FilmsBranchScreen : SupportAppScreen() {
    override fun getFragment() = FilmsBranchFragment()
}

object FavoritesBranchScreen : SupportAppScreen() {
    override fun getFragment() = FavoritesBranchFragment()
}

object ProfileBranchScreen : SupportAppScreen() {
    override fun getFragment() = ProfileBranchFragment()
}