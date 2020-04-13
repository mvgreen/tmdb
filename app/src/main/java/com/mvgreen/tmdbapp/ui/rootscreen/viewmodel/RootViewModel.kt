package com.mvgreen.tmdbapp.ui.rootscreen.viewmodel

import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.ui.cicerone.FilmsBranchScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class RootViewModel @Inject constructor() : BaseViewModel() {

    var currentBranch: SupportAppScreen? = null

}