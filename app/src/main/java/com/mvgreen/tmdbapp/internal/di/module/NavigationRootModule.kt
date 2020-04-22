package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.NavigationRootScope
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
class NavigationRootModule {

    @Provides
    @NavigationRootScope
    fun cicerone(): Cicerone<SelfRestoringRouter> = Cicerone.create(SelfRestoringRouter())

    @Provides
    @NavigationRootScope
    fun navigatorHolder(cicerone: Cicerone<SelfRestoringRouter>): NavigatorHolder =
        cicerone.navigatorHolder

    @Provides
    @NavigationRootScope
    fun router(cicerone: Cicerone<SelfRestoringRouter>): SelfRestoringRouter =
        cicerone.router

}