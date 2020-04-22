package com.mvgreen.tmdbapp.internal.di.component

import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.BranchViewModel
import ru.terrakok.cicerone.NavigatorHolder

interface CiceroneOwner {

    fun navigatorHolder(): NavigatorHolder

    fun router(): SelfRestoringRouter

    fun branchViewModel(): BranchViewModel

}