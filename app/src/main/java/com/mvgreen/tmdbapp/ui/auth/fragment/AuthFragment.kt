package com.mvgreen.tmdbapp.ui.auth.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.data.exception.ConnectionException
import com.mvgreen.data.exception.CredentialsException
import com.mvgreen.data.exception.InvalidInputException
import com.mvgreen.data.exception.ServerException
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.activity.AppActivity
import com.mvgreen.tmdbapp.ui.base.event.Event
import com.mvgreen.tmdbapp.ui.base.event.LoginFailedEvent
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.delegator.DisappearOnEnterExpandOnExitStrategy
import com.mvgreen.tmdbapp.ui.delegator.ShowFirstInitialState
import com.mvgreen.tmdbapp.ui.delegator.Visibility
import com.mvgreen.tmdbapp.ui.auth.viewmodel.AuthViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.observe
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.concurrent.TimeoutException


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
        val email = input_login.text.toString()
        val password = input_password.text.toString()
        btn_login.isEnabled = false
        label_error.visibility = View.GONE

        with(requireActivity() as AppActivity) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        viewModel.onLogin(email, password)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity() as AppActivity

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
        input_password.setOnEditorActionListener { _, _, _ ->
            btn_login.requestFocus()
            btn_login.performClick()
        }
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.authViewModel()
        })

        observe(viewModel.events, this::onEvent)
        input_login
            .textChanges()
            .map { it.toString() }
            .retry()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { btn_login.isEnabled = true },
                { e -> Log.e(TAG, e.message, e) }
            )
            .disposeOnViewModelDestroy()
    }

    private fun setupDelegator() {
        stateDelegate = StateDelegate(
            State(
                Visibility.VISIBLE,
                listOf(title, subtitile),
                DisappearOnEnterExpandOnExitStrategy(requireContext())
            ),
            initialState = ShowFirstInitialState
        )
    }

    private fun onKeyboardVisible(visible: Boolean) {
        stateDelegate.currentState = if (visible) Visibility.VISIBLE else Visibility.GONE
    }

    private fun onEvent(event: Event) {
        if (event is LoginFailedEvent) {
            onLoginError(event.e)
            btn_login.isEnabled = true
        } else {
            Log.e(TAG, "Unexpected event: ${event::class.java}")
            Snackbar.make(requireView(), R.string.error_not_implemented, Snackbar.LENGTH_SHORT)
        }
    }

    private fun onLoginError(exception: Throwable?) {
        label_error.text = getString(
            when (exception) {
                is ConnectionException -> R.string.error_no_connection
                is CredentialsException -> R.string.error_bad_credentials
                is InvalidInputException -> R.string.error_bad_credentials
                is ServerException -> R.string.error_server
                is TimeoutException -> R.string.error_bad_connection
                else -> R.string.error_unknown
            }
        )
        label_error.visibility = View.VISIBLE
    }

}