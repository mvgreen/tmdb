package com.mvgreen.tmdbapp.internal.di.module

import android.content.Context
import com.mvgreen.data.network.auth.AuthRepositoryImpl
import com.mvgreen.data.network.auth.RefreshRepository
import com.mvgreen.data.network.auth.api.ApiHolder
import com.mvgreen.data.network.auth.api.AuthApi
import com.mvgreen.data.network.authenticator.TokenAuthenticator
import com.mvgreen.data.network.factory.TMDbApiFactory
import com.mvgreen.data.network.factory.TMDbGuestApiFactory
import com.mvgreen.data.network.image.ImageRepositoryImpl
import com.mvgreen.data.network.image.api.ImageConfigurationApi
import com.mvgreen.data.network.interceptor.HttpErrorInterceptor
import com.mvgreen.data.network.search.SearchRepositoryImpl
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.data.storage.GenreStorageImpl
import com.mvgreen.data.storage.ImageConfigStorageImpl
import com.mvgreen.data.storage.UserDataStorageImpl
import com.mvgreen.data.usecase.*
import com.mvgreen.domain.repository.*
import com.mvgreen.domain.usecase.*
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
        return TMDbGuestApiFactory(
            HttpErrorInterceptor(),
            MoshiConverterFactory.create(moshiInstance)
        ).create(SearchApi::class.java)
    }

    @Provides
    @ApplicationScope
    fun provideImageApi(
        moshiInstance: Moshi
    ): ImageConfigurationApi {
        return TMDbGuestApiFactory(
            HttpErrorInterceptor(),
            MoshiConverterFactory.create(moshiInstance)
        ).create(ImageConfigurationApi::class.java)
    }

    /** Репозитории */

    @Provides
    @ApplicationScope
    fun credentialsStorage(context: Context): UserDataStorage = UserDataStorageImpl(context)

    @Provides
    @ApplicationScope
    fun authRepository(api: AuthApi): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @ApplicationScope
    fun genreStorage(): GenreStorage = GenreStorageImpl()

    @Provides
    @ApplicationScope
    fun searchRepository(api: SearchApi, genreStorage: GenreStorage): SearchRepository =
        SearchRepositoryImpl(api, genreStorage)

    @Provides
    @ApplicationScope
    fun imageRepository(imageConfigurationApi: ImageConfigurationApi): ImageRepository =
        ImageRepositoryImpl(imageConfigurationApi)

    @Provides
    @ApplicationScope
    fun imageConfigStorage(context: Context): ImageConfigStorage = ImageConfigStorageImpl(context)


    /** UseCase */

    @Provides
    @ApplicationScope
    fun authUseCase(
        authRepository: AuthRepository,
        userDataStorage: UserDataStorage
    ): AuthUseCase =
        AuthUseCaseImpl(authRepository, userDataStorage)

    @Provides
    @ApplicationScope
    fun profileUseCase(userDataStorage: UserDataStorage): ProfileUseCase =
        ProfileUseCaseImpl(userDataStorage)

    @Provides
    @ApplicationScope
    fun searchUseCase(
        searchRepository: SearchRepository,
        genreStorage: GenreStorage
    ): SearchUseCase =
        SearchUseCaseImpl(searchRepository, genreStorage)

    @Provides
    @ApplicationScope
    fun loadImageUseCase(
        imageRepository: ImageRepository,
        imageConfigStorage: ImageConfigStorage,
        userDataStorage: UserDataStorage
    ): LoadImageUseCase =
        LoadImageUseCaseImpl(imageRepository, imageConfigStorage, userDataStorage)

    @Provides
    @ApplicationScope
    fun detailsUseCase(
        searchRepository: SearchRepository
    ): DetailsUseCase =
        DetailsUseCaseImpl(searchRepository)

}