package com.mvgreen.tmdbapp.internal.di.component

import android.content.Context
import com.mvgreen.tmdbapp.internal.di.module.ProfileModule
import com.mvgreen.tmdbapp.internal.di.scope.ProfileTabScope
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.BranchViewModel
import dagger.BindsInstance
import dagger.Component
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Component(modules = [ProfileModule::class])
@ProfileTabScope
internal interface ProfileTabComponent : CiceroneOwner {

    fun cicerone(): Cicerone<SelfRestoringRouter>

    override fun router(): SelfRestoringRouter

    override fun navigatorHolder(): NavigatorHolder

    override fun branchViewModel(): BranchViewModel

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(appContext: Context): Builder

        fun build(): ProfileTabComponent

    }

}