package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.data.network.auth.api.TMDbApi
import com.mvgreen.domain.repository.AuthRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.ProfileUseCase
import com.mvgreen.tmdbapp.internal.di.module.AppModule
import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import com.mvgreen.tmdbapp.ui.auth.viewmodel.AuthViewModel
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.RootViewModel
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Component(modules = [AppModule::class])
@ApplicationScope
internal interface ApplicationComponent {

    fun cicerone(): Cicerone<Router>

    fun navigatorHolder(): NavigatorHolder

    fun router(): Router

    fun authViewModel(): AuthViewModel

    fun rootViewModel(): RootViewModel

    fun moshi(): Moshi

    fun api(): TMDbApi

    fun authRepository(): AuthRepository

    fun userDataStorage(): UserDataStorage

    fun authUseCase(): AuthUseCase

    fun profileUseCase(): ProfileUseCase

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): ApplicationComponent

    }

}