package com.mvgreen.tmdbapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.tmdbapp.internal.di.DI
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CiceroneScopeTest {

    lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun ciceroneInstancesAreUnique() {
        // given
        DI.init(appContext)

        // when
        val globalCicerone1 = DI.appComponent.cicerone()
        val globalCicerone2 = DI.appComponent.cicerone()

        val favoritesCicerone1 = DI.favoritesTabComponent.cicerone()
        val favoritesCicerone2 = DI.favoritesTabComponent.cicerone()

        val filmsCicerone1 = DI.filmTabComponent.cicerone()
        val filmsCicerone2 = DI.filmTabComponent.cicerone()

        val profileCicerone1 = DI.profileTabComponent.cicerone()
        val profileCicerone2 = DI.profileTabComponent.cicerone()

        // then
        assertTrue(globalCicerone1 === globalCicerone2)
        assertTrue(favoritesCicerone1 === favoritesCicerone2)
        assertTrue(filmsCicerone1 === filmsCicerone2)
        assertTrue(profileCicerone1 === profileCicerone2)

        assertTrue(globalCicerone1 !== favoritesCicerone1)
        assertTrue(globalCicerone1 !== filmsCicerone1)
        assertTrue(globalCicerone1 !== profileCicerone1)

        assertTrue(favoritesCicerone1 !== filmsCicerone1)
        assertTrue(favoritesCicerone1 !== profileCicerone1)

        assertTrue(filmsCicerone1 !== profileCicerone1)
    }

}