package com.mvgreen.tmdbapp.ui.profile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.ProfileScreen
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class ProfileBranchFragment : BaseFragment(R.layout.branch_profile) {

    private val branchNavigatorHolder = DI.profileTabComponent.navigatorHolder()
    private val branchRouter = DI.profileTabComponent.router()
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
        branchRouter.newRootScreen(ProfileScreen)
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