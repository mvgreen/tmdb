package com.mvgreen.tmdbapp.ui.fragment

import android.os.Bundle
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.MainActivity
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as MainActivity

        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}
