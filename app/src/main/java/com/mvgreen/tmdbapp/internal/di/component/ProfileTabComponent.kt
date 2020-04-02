package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.module.ProfileModule
import com.mvgreen.tmdbapp.internal.di.scope.ProfileTabScope
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Component(modules = [ProfileModule::class])
@ProfileTabScope
internal interface ProfileTabComponent {

    fun cicerone(): Cicerone<Router>

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): ProfileTabComponent
    }

}