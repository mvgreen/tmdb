package com.mvgreen.tmdbapp.internal.di.module

import android.content.Context
import com.mvgreen.data.network.auth.AuthRepositoryImpl
import com.mvgreen.data.network.auth.RefreshRepository
import com.mvgreen.data.network.auth.api.ApiHolder
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.data.network.authenticator.TokenAuthenticator
import com.mvgreen.data.network.factory.TMDbApiFactory
import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import com.mvgreen.data.storage.UserDataStorageImpl
import com.mvgreen.data.usecase.AuthUseCaseImpl
import com.mvgreen.data.usecase.ProfileUseCaseImpl
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
internal class AppModule {

    /** Навигация */

    @Provides
    @ApplicationScope
    fun cicerone(): Cicerone<SelfRestoringRouter> = Cicerone.create(SelfRestoringRouter())

    @Provides
    @ApplicationScope
    fun navigatorHolder(cicerone: Cicerone<SelfRestoringRouter>): NavigatorHolder =
        cicerone.navigatorHolder

    @Provides
    @ApplicationScope
    fun router(cicerone: Cicerone<SelfRestoringRouter>): SelfRestoringRouter =
        cicerone.router

    /** API */

    // Встраивать дополнительные конвертеры тут
    @Provides
    @ApplicationScope
    fun provideMoshiInstance(): Moshi = Moshi.Builder().build()

    @Provides
    @ApplicationScope
    fun apiHolder(): ApiHolder = ApiHolder()

    @Provides
    @ApplicationScope
    fun authenticator(
        refreshRepository: RefreshRepository,
        userDataStorage: UserDataStorage
    ): Authenticator = TokenAuthenticator(refreshRepository, userDataStorage)

    @Provides
    @ApplicationScope
    fun refreshRepository(apiHolder: ApiHolder): RefreshRepository = RefreshRepository(apiHolder)

    @Provides
    @ApplicationScope
    fun provideApi(
        moshiInstance: Moshi,
        authenticator: Authenticator,
        apiHolder: ApiHolder
    ): TMDbApi {
        apiHolder.api = TMDbApiFactory(
            HttpErrorInterceptor(),
            authenticator,
            MoshiConverterFactory.create(moshiInstance)
        ).create(TMDbApi::class.java)

        return apiHolder.api
    }

    /** Репозитории */

    @Provides
    @ApplicationScope
    fun credentialsStorage(context: Context): UserDataStorage = UserDataStorageImpl(context)

    @Provides
    @ApplicationScope
    fun authRepository(api: TMDbApi): AuthRepository = AuthRepositoryImpl(api)

    /** UseCase-ы */

    @Provides
    @ApplicationScope
    fun authUseCase(authRepository: AuthRepository, userDataStorage: UserDataStorage): AuthUseCase =
        AuthUseCaseImpl(authRepository, userDataStorage)

    @Provides
    @ApplicationScope
    fun profileUseCase(userDataStorage: UserDataStorage): ProfileUseCase =
        ProfileUseCaseImpl(userDataStorage)

}