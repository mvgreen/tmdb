package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.FavoritesTabScope
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
internal class FavoritesModule {

    @Provides
    @FavoritesTabScope
    fun cicerone(): Cicerone<SelfRestoringRouter> = Cicerone.create(SelfRestoringRouter())

    @Provides
    @FavoritesTabScope
    fun navigatorHolder(cicerone: Cicerone<SelfRestoringRouter>): NavigatorHolder =
        cicerone.navigatorHolder

    @Provides
    @FavoritesTabScope
    fun router(cicerone: Cicerone<SelfRestoringRouter>): SelfRestoringRouter =
        cicerone.router

}