package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Module
internal class AppModule {

    @Provides
    @ApplicationScope
    fun cicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @ApplicationScope
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.navigatorHolder

    @Provides
    @ApplicationScope
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router
}