package com.mvgreen.tmdbapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.MainActivity
import com.mvgreen.tmdbapp.ui.base.event.Event
import com.mvgreen.tmdbapp.ui.base.event.LoginFailedEvent
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.delegator.CollapseOnEnterExpandOnExitStrategy
import com.mvgreen.tmdbapp.ui.delegator.ShowFirstInitialState
import com.mvgreen.tmdbapp.ui.delegator.Visibility
import com.mvgreen.tmdbapp.ui.viewmodel.AuthViewModel
import com.mvgreen.tmdbapp.utils.emailValid
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.observe
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    companion object {
        private const val TAG = "AuthFragment"
    }

    private lateinit var stateDelegate: StateDelegate<Visibility>
    private lateinit var viewModel: AuthViewModel

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

    private val onLoginListener = View.OnClickListener {
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        btn_login.isEnabled = false
        error_label.visibility = View.GONE
        viewModel.onLogin(email, password)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as MainActivity

        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        setupView()
        setupViewModel()
        setupDelegator()
    }

    override fun onResume() {
        super.onResume()
        bindToFragmentLifecycle(onKeyboardEventListener)
    }

    private fun setupView() {
        btn_login.setOnClickListener(onLoginListener)
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.authViewModel()
        })

        observe(viewModel.events, this::onEvent)
        input_email
            .textChanges()
            .map { it.toString() }
            .retry()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { email ->
                    btn_login.isEnabled = emailValid(email) && !viewModel.loginInProgress
                },
                { e -> Log.e(TAG, e.message, e) }
            )
            .disposeOnViewModelDestroy()
    }

    private fun setupDelegator() {
        stateDelegate = StateDelegate(
            State(
                Visibility.VISIBLE,
                listOf(title, subtitile),
                CollapseOnEnterExpandOnExitStrategy(requireContext())
            ),
            initialState = ShowFirstInitialState
        )
    }

    private fun onKeyboardVisible(visible: Boolean) {
        stateDelegate.currentState = if (visible) Visibility.VISIBLE else Visibility.GONE
    }

    private fun onEvent(event: Event) {
        if (event is LoginFailedEvent) {
            btn_login.isEnabled =
                emailValid(input_email.text?.toString() ?: "") && !viewModel.loginInProgress
            error_label.visibility = View.VISIBLE
        }
    }
}