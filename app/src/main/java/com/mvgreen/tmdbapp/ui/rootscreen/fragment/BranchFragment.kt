package com.mvgreen.tmdbapp.ui.rootscreen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.internal.di.component.CiceroneOwner
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.FavoritesScreen
import com.mvgreen.tmdbapp.ui.cicerone.FilmsScreen
import com.mvgreen.tmdbapp.ui.cicerone.ProfileScreen
import com.mvgreen.tmdbapp.ui.rootscreen.viewmodel.BranchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command

class BranchFragment constructor(private var branchId: Int) :
    BaseFragment(R.layout.branch_container) {

    constructor() : this(BRANCH_NONE)

    companion object {
        const val BRANCH_ID = "BRANCH_ID"

        const val BRANCH_NONE = -1
        const val BRANCH_FILMS = 0
        const val BRANCH_FAVORITES = 1
        const val BRANCH_PROFILE = 2
    }

    private lateinit var viewModel: BranchViewModel

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
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }
        val (ciceroneOwner, rootScreen) = getCiceroneInstances()
        setupViewModel(ciceroneOwner)
        viewModel.branchRouter.restore(rootScreen, savedInstanceState != null)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setNavigator(branchNavigator)
    }

    override fun onPause() {
        viewModel.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        viewModel.branchRouter.exit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BRANCH_ID, branchId)
    }

    private fun setupViewModel(
        ciceroneOwner: CiceroneOwner
    ) {
        viewModel = getViewModel(viewModelFactory {
            ciceroneOwner.branchViewModel()
        })
        viewModel.init(ciceroneOwner.navigatorHolder(), ciceroneOwner.router())
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        branchId = savedInstanceState.getInt(BRANCH_ID, BRANCH_NONE)
    }

    private fun getCiceroneInstances(): Pair<CiceroneOwner, SupportAppScreen> {
        return when (branchId) {
            BRANCH_FAVORITES -> Pair(DI.favoritesTabComponent, FavoritesScreen)
            BRANCH_FILMS -> Pair(DI.filmsTabComponent, FilmsScreen)
            BRANCH_PROFILE -> Pair(DI.profileTabComponent, ProfileScreen)
            else -> throw IllegalStateException("Branch ID not found")
        }
    }
}