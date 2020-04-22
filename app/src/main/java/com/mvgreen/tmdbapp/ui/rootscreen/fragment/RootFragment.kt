package com.mvgreen.tmdbapp.ui.rootscreen.fragment

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.FavoritesBranchScreen
import com.mvgreen.tmdbapp.ui.cicerone.FilmsBranchScreen
import com.mvgreen.tmdbapp.ui.cicerone.ProfileBranchScreen
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.RootViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import kotlinx.android.synthetic.main.fragment_root.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

class RootFragment @Inject constructor() : BaseFragment(R.layout.fragment_root) {

    private lateinit var viewModel: RootViewModel

    private val childNavigatorHolder = DI.navigationRootComponent.navigatorHolder()
    private val childRouter = DI.navigationRootComponent.router()
    private val childNavigator: Navigator by lazy {
        object : SupportAppNavigator(
            requireActivity(),
            childFragmentManager,
            R.id.child_container
        ) {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            val newBranch = when (item.itemId) {
                R.id.it_favorite -> FavoritesBranchScreen
                R.id.it_films -> FilmsBranchScreen
                R.id.it_profile -> ProfileBranchScreen
                else -> throw IllegalStateException()
            }
            if (newBranch != viewModel.currentBranch) {
                viewModel.currentBranch = newBranch
                childRouter.newRootScreen(newBranch)
            }

            true
        }
        if (viewModel.currentBranch == null) {
            viewModel.currentBranch = FilmsBranchScreen
            childRouter.newRootScreen(viewModel.currentBranch!!)
        }
    }

    override fun onResume() {
        super.onResume()
        changeSystemColors(R.color.bg_black, R.color.widget_blue)
        childNavigatorHolder.setNavigator(childNavigator)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onPause() {
        childNavigatorHolder.removeNavigator()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.fragments.lastOrNull()

        if (fragment != null && fragment is BaseFragment) {
            if (!fragment.onBackPressed()) {
                return false
            }
        } else {
            return false
        }
        return true
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.rootViewModel()
        })
    }

}