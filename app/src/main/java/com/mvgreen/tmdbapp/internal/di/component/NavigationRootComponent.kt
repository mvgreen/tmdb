package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.module.NavigationRootModule
import com.mvgreen.tmdbapp.internal.di.scope.NavigationRootScope
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Component(modules = [NavigationRootModule::class])
@NavigationRootScope
internal interface NavigationRootComponent : CiceroneOwner {

    fun cicerone(): Cicerone<Router>

    override fun router(): Router

    override fun navigatorHolder(): NavigatorHolder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): NavigationRootComponent

    }

}