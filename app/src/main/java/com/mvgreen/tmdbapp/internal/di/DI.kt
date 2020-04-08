package com.mvgreen.tmdbapp.internal.di

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.component.*

internal object DI {

    lateinit var appComponent: ApplicationComponent
        private set

    lateinit var filmTabComponent: FilmsTabComponent
        private set

    lateinit var favoritesTabComponent: FavoritesTabComponent
        private set

    lateinit var profileTabComponent: ProfileTabComponent
        private set

    lateinit var navigationRootComponent: NavigationRootComponent
        private set


    fun init(appContext: Context) {
        appComponent = DaggerApplicationComponent.builder().context(appContext).build()
        filmTabComponent = DaggerFilmsTabComponent.builder().context(appContext).build()
        favoritesTabComponent = DaggerFavoritesTabComponent.builder().context(appContext).build()
        profileTabComponent = DaggerProfileTabComponent.builder().context(appContext).build()
        navigationRootComponent = DaggerNavigationRootComponent.builder().context(appContext).build()
    }

}
