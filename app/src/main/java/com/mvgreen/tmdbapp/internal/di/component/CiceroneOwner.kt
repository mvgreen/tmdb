package com.mvgreen.tmdbapp.internal.di.component

import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

interface CiceroneOwner {

    fun navigatorHolder(): NavigatorHolder

    fun router(): Router

}