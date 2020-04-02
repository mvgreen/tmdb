package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.FavoritesTabScope
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Module
internal class FavoritesModule {

    @Provides
    @FavoritesTabScope
    fun cicerone(): Cicerone<Router> = Cicerone.create()

}