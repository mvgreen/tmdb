package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.ProfileTabScope
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
internal class ProfileModule {

    @Provides
    @ProfileTabScope
    fun cicerone(): Cicerone<SelfRestoringRouter> = Cicerone.create(SelfRestoringRouter())

    @Provides
    @ProfileTabScope
    fun navigatorHolder(cicerone: Cicerone<SelfRestoringRouter>): NavigatorHolder =
        cicerone.navigatorHolder

    @Provides
    @ProfileTabScope
    fun router(cicerone: Cicerone<SelfRestoringRouter>): SelfRestoringRouter =
        cicerone.router

}