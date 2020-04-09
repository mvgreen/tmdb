package com.mvgreen.tmdbapp.internal.di.module

import android.content.Context
import com.mvgreen.data.network.auth.AuthRepositoryImpl
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.factory.TMDbApiFactory
import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import com.mvgreen.data.storage.CredentialsStorageImpl
import com.mvgreen.data.usecase.AuthUseCaseImpl
import com.mvgreen.data.usecase.ProfileUseCaseImpl
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.CredentialsStorage
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
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
            HttpErrorInterceptor(moshiInstance),
            MoshiConverterFactory.create(moshiInstance)
        ).create(TMDbApi::class.java)
    }

    /** Репозитории */

    @Provides
    @ApplicationScope
    fun credentialsStorage(context: Context): CredentialsStorage = CredentialsStorageImpl(context)

    @Provides
    @ApplicationScope
    fun authRepository(api: TMDbApi): AuthRepository = AuthRepositoryImpl(api)

    /** UseCase-ы */

    @Provides
    @ApplicationScope
    fun authUseCase(authRepository: AuthRepository, credentialsStorage: CredentialsStorage): AuthUseCase =
        AuthUseCaseImpl(authRepository, credentialsStorage)

    @Provides
    @ApplicationScope
    fun profileUseCase(authRepository: AuthRepository, credentialsStorage: CredentialsStorage): ProfileUseCase =
        ProfileUseCaseImpl(authRepository, credentialsStorage)

}