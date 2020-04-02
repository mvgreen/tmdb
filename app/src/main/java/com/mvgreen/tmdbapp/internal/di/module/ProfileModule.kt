package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.tmdbapp.internal.di.scope.ProfileTabScope
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Module
internal class ProfileModule {

    @Provides
    @ProfileTabScope
    fun cicerone(): Cicerone<Router> = Cicerone.create()

}