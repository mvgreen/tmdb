package com.mvgreen.tmdbapp.ui.rootscreen.viewmodel

import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.ui.cicerone.SelfRestoringRouter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class BranchViewModel @Inject constructor() : BaseViewModel() {

    lateinit var branchNavigatorHolder: NavigatorHolder
    lateinit var branchRouter: SelfRestoringRouter
    private var currentScreen: SupportAppScreen? = null

    fun init(
        navigatorHolder: NavigatorHolder,
        router: SelfRestoringRouter,
        rootScreen: SupportAppScreen
    ) {
        branchNavigatorHolder = navigatorHolder
        branchRouter = router
        branchRouter.restore(rootScreen)
    }

    fun setNavigator(navigator: Navigator) {
        branchNavigatorHolder.setNavigator(navigator)
    }

    fun removeNavigator() {
        branchNavigatorHolder.removeNavigator()
    }

}