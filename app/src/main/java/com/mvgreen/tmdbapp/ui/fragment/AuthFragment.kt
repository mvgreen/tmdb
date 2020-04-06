package com.mvgreen.tmdbapp.ui.fragment

import android.os.Bundle
import android.view.ViewTreeObserver
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.MainActivity
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.delegator.CollapseOnEnterExpandOnExitStrategy
import com.mvgreen.tmdbapp.ui.delegator.ShowFirstInitialState
import com.mvgreen.tmdbapp.ui.delegator.Visibility
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private lateinit var stateDelegate: StateDelegate<Visibility>

    private val onKeyboardEventListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var keyboardVisible: Boolean = false
        override fun onGlobalLayout() {
            val root = view ?: return
            val heightDiff = root.rootView.height - root.height
            if (heightDiff > resources.getDimension(R.dimen.keyboard_min_size)) {
                if (!keyboardVisible) onKeyboardVisible(true)
                keyboardVisible = true
            } else if (keyboardVisible) {
                onKeyboardVisible(false)
                keyboardVisible = false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as MainActivity

        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDelegator()
    }

    override fun onResume() {
        super.onResume()
        bindToFragmentLifecycle(onKeyboardEventListener)
    }

    private fun setupDelegator() {
        stateDelegate = StateDelegate(
            State(
                Visibility.VISIBLE,
                listOf(title, animated_margin),
                CollapseOnEnterExpandOnExitStrategy(requireContext())
            ),
            initialState = ShowFirstInitialState
        )
    }

    private fun onKeyboardVisible(visible: Boolean) {
        stateDelegate.currentState = if (visible) Visibility.VISIBLE else Visibility.GONE
    }
}