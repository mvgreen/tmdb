package com.mvgreen.tmdbapp.internal.di.module

import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.factory.TMDbApiFactory
import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Module
internal class AppModule {

    /** Навигация */

    @Provides
    @ApplicationScope
    fun cicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @ApplicationScope
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.navigatorHolder

    @Provides
    @ApplicationScope
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router

    /** API */

    // Встраивать дополнительные конвертеры тут
    @Provides
    @ApplicationScope
    fun provideMoshiInstance(): Moshi = Moshi.Builder().build()

    @Provides
    @ApplicationScope
    fun provideApi(
        moshiInstance: Moshi
    ): TMDbApi {
        return TMDbApiFactory(
            MoshiConverterFactory.create(moshiInstance)
        ).create(TMDbApi::class.java)
    }
}