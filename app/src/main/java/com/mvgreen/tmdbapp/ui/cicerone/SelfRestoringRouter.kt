package com.mvgreen.tmdbapp.ui.cicerone

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class SelfRestoringRouter : Router() {

    private var currentScreen: SupportAppScreen? = null

    fun restore(startScreen: SupportAppScreen) {
        if (currentScreen == null) {
            currentScreen = startScreen
            newRootScreen(startScreen)
        }
    }

}