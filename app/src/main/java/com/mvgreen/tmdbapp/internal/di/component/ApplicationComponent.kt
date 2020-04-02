package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.module.AppModule
import com.mvgreen.tmdbapp.internal.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Component(modules = [AppModule::class])
@ApplicationScope
internal interface ApplicationComponent {

    fun cicerone(): Cicerone<Router>

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): ApplicationComponent
    }

}