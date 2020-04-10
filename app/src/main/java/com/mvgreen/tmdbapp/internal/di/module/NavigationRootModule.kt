package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.NavigationRootScope
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Module
class NavigationRootModule {

    @Provides
    @NavigationRootScope
    fun cicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @NavigationRootScope
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.navigatorHolder

    @Provides
    @NavigationRootScope
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router

}