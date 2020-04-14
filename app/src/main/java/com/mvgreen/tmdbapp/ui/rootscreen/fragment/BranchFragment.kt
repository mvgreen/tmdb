package com.mvgreen.tmdbapp.ui.rootscreen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.component.CiceroneOwner
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command

class BranchFragment(
    ciceroneOwner: CiceroneOwner,
    private val rootScreen: SupportAppScreen
) : BaseFragment(R.layout.branch_container) {

    private val branchNavigatorHolder: NavigatorHolder = ciceroneOwner.navigatorHolder()
    private val branchRouter: Router = ciceroneOwner.router()

    private val branchNavigator: Navigator by lazy {
        object : SupportAppNavigator(
            requireActivity(),
            childFragmentManager,
            R.id.profile_branch_container
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
        branchRouter.newRootScreen(rootScreen)
    }

    override fun onResume() {
        super.onResume()
        branchNavigatorHolder.setNavigator(branchNavigator)
    }

    override fun onPause() {
        branchNavigatorHolder.removeNavigator()
        super.onPause()
    }

}