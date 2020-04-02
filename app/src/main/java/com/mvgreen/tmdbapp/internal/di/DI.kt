package com.mvgreen.tmdbapp.internal.di

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.component.*
import com.mvgreen.tmdbapp.internal.di.component.ApplicationComponent
import com.mvgreen.tmdbapp.internal.di.component.FavoritesTabComponent
import com.mvgreen.tmdbapp.internal.di.component.FilmsTabComponent
import com.mvgreen.tmdbapp.internal.di.component.ProfileTabComponent

internal object DI {

    lateinit var appComponent: ApplicationComponent
        private set

    lateinit var filmTabComponent: FilmsTabComponent
        private set

    lateinit var favoritesTabComponent: FavoritesTabComponent
        private set

    lateinit var profileTabComponent: ProfileTabComponent
        private set


    fun init(appContext: Context) {
        appComponent = DaggerApplicationComponent.builder().context(appContext).build()
        filmTabComponent = DaggerFilmsTabComponent.builder().context(appContext).build()
        favoritesTabComponent = DaggerFavoritesTabComponent.builder().context(appContext).build()
        profileTabComponent = DaggerProfileTabComponent.builder().context(appContext).build()
    }

}
