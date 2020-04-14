package com.mvgreen.tmdbapp.ui.search

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment(R.layout.fragment_search) {

    override fun onResume() {
        super.onResume()
        input_search.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(input_search, InputMethodManager.SHOW_IMPLICIT)
    }

}