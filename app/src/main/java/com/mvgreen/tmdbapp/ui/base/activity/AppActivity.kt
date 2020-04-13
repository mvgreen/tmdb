package com.mvgreen.tmdbapp.ui.base.activity

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

abstract class AppActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AppActivity"
    }

    private lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigatorHolder = DI.appComponent.navigatorHolder()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.fragments.lastOrNull()

        if (fragment != null && fragment is BaseFragment) {
            if (!fragment.onBackPressed())
                super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    fun updateStatusBar(colorId: Int) {
        val color: Int
        try {
            color = ContextCompat.getColor(this, colorId)
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, e.message ?: e::class.toString())
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    fun updateNavigationBar(colorId: Int) {
        val color: Int
        try {
            color = ContextCompat.getColor(this, colorId)
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, e.message ?: e::class.toString())
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }

    fun updateDividerColor(colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val color: Int
            try {
                color = ContextCompat.getColor(this, colorId)
            } catch (e: Resources.NotFoundException) {
                Log.e(TAG, e.message ?: e::class.toString())
                return
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarDividerColor = color
        }
    }

}