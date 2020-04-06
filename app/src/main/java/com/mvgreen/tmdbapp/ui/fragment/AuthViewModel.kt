package com.mvgreen.tmdbapp.ui.fragment

import com.mvgreen.tmdbapp.ui.base.event.LoginFailedEvent
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.utils.onNext
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthViewModel @Inject constructor() : BaseViewModel() {

    var loginInProgress: Boolean = false

    fun onLogin(email: String, password: String) {
        loginInProgress = true
        Single
            .timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _, _ ->
                loginInProgress = false
                events.onNext(LoginFailedEvent)
            }
            .disposeOnViewModelDestroy()
    }
}