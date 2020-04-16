package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.data.network.auth.api.AuthApi
import com.mvgreen.data.network.image.api.ImageConfigurationApi
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.domain.repository.*
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.internal.di.module.AppModule
import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import com.mvgreen.tmdbapp.ui.auth.viewmodel.AuthViewModel
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.RootViewModel
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Component(modules = [AppModule::class])
@ApplicationScope
internal interface ApplicationComponent {

    /** Cicerone */

    fun cicerone(): Cicerone<SelfRestoringRouter>

    fun navigatorHolder(): NavigatorHolder

    fun router(): SelfRestoringRouter

    /** ViewModel */

    fun authViewModel(): AuthViewModel

    fun rootViewModel(): RootViewModel

    /** API */

    fun moshi(): Moshi

    fun authApi(): AuthApi

    fun searchApi(): SearchApi

    fun imageConfigurationApi(): ImageConfigurationApi

    /** Repository/Storage */

    fun authRepository(): AuthRepository

    fun userDataStorage(): UserDataStorage

    fun searchRepository(): SearchRepository

    fun genreStorage(): GenreStorage

    fun imageRepository(): ImageRepository

    fun imageConfigStorage(): ImageConfigStorage

    /** UseCase */

    fun authUseCase(): AuthUseCase

    fun profileUseCase(): ProfileUseCase

    fun searchUseCase(): SearchUseCase

    fun loadImageUseCase(): LoadImageUseCase

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): ApplicationComponent

    }

}