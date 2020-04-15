package com.mvgreen.tmdbapp.internal.di.module

import android.content.Context
import androidx.room.Room
import com.mvgreen.data.network.auth.AuthRepositoryImpl
import com.mvgreen.data.network.auth.RefreshRepository
import com.mvgreen.data.network.auth.api.ApiHolder
import com.mvgreen.data.network.auth.api.AuthApi
import com.mvgreen.data.network.authenticator.TokenAuthenticator
import com.mvgreen.data.network.factory.SearchApiFactory
import com.mvgreen.data.network.factory.TMDbApiFactory
import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import com.mvgreen.data.network.search.SearchRepositoryImpl
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.data.storage.GenreStorageImpl
import com.mvgreen.data.storage.UserDataStorageImpl
import com.mvgreen.data.storage.db.GenreDao
import com.mvgreen.data.storage.db.GenreDb
import com.mvgreen.data.usecase.AuthUseCaseImpl
import com.mvgreen.data.usecase.ProfileUseCaseImpl
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
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
    fun provideAuthApi(
        moshiInstance: Moshi,
        authenticator: Authenticator,
        apiHolder: ApiHolder
    ): AuthApi {
        apiHolder.api = TMDbApiFactory(
            HttpErrorInterceptor(),
            authenticator,
            MoshiConverterFactory.create(moshiInstance)
        ).create(AuthApi::class.java)

        return apiHolder.api
    }

    @Provides
    @ApplicationScope
    fun provideSearchApi(
        moshiInstance: Moshi
    ): SearchApi {
        return SearchApiFactory(
            HttpErrorInterceptor(),
            MoshiConverterFactory.create(moshiInstance)
        ).create(SearchApi::class.java)
    }

    /** БД */

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Context): GenreDb =
        Room
            .databaseBuilder(context, GenreDb::class.java, "tasks")
            .build()

    @Provides
    @ApplicationScope
    fun provideDao(db: GenreDb): GenreDao = db.genreDao()

    /** Репозитории */

    @Provides
    @ApplicationScope
    fun credentialsStorage(context: Context): UserDataStorage = UserDataStorageImpl(context)

    @Provides
    @ApplicationScope
    fun authRepository(api: AuthApi): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @ApplicationScope
    fun genreStorage(genreDao: GenreDao): GenreStorage = GenreStorageImpl(genreDao)

    @Provides
    @ApplicationScope
    fun searchRepository(api: SearchApi, genreStorage: GenreStorage): SearchRepository =
        SearchRepositoryImpl(api, genreStorage)


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