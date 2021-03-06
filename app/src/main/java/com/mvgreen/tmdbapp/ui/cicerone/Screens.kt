package com.mvgreen.tmdbapp.ui.cicerone

import com.mvgreen.tmdbapp.ui.auth.fragment.AuthFragment
import com.mvgreen.tmdbapp.ui.details.fragment.DetailsFragment
import com.mvgreen.tmdbapp.ui.favorites.fragment.FavoritesBranchFragment
import com.mvgreen.tmdbapp.ui.films.fragment.FilmsWelcomeFragment
import com.mvgreen.tmdbapp.ui.launch.fragment.LaunchFragment
import com.mvgreen.tmdbapp.ui.profile.fragment.ProfileFragment
import com.mvgreen.tmdbapp.ui.rootscreen.fragment.BranchFragment
import com.mvgreen.tmdbapp.ui.rootscreen.fragment.RootFragment
import com.mvgreen.tmdbapp.ui.search.fragment.SearchFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object AuthScreen : SupportAppScreen() {
    override fun getFragment() = AuthFragment()
}

object MainScreen : SupportAppScreen() {
    override fun getFragment() = RootFragment()
}

object FilmsBranchScreen : SupportAppScreen() {
    override fun getFragment() = BranchFragment(BranchFragment.BRANCH_FILMS)
}

object FavoritesBranchScreen : SupportAppScreen() {
    override fun getFragment() = BranchFragment(BranchFragment.BRANCH_FAVORITES)
}

object ProfileBranchScreen : SupportAppScreen() {
    override fun getFragment() = BranchFragment(BranchFragment.BRANCH_PROFILE)
}

object ProfileScreen : SupportAppScreen() {
    override fun getFragment() = ProfileFragment()
}

object FilmsScreen : SupportAppScreen() {
    override fun getFragment() = FilmsWelcomeFragment()
}

object FavoritesScreen : SupportAppScreen() {
    override fun getFragment() = FavoritesBranchFragment()
}

object SearchScreen : SupportAppScreen() {
    override fun getFragment() = SearchFragment()
}

object LaunchScreen: SupportAppScreen() {
    override fun getFragment() = LaunchFragment()
}

class DetailsScreen(private val movieId: Int): SupportAppScreen() {
    override fun getFragment() = DetailsFragment(movieId)
}