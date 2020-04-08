package com.mvgreen.tmdbapp.ui.auth.viewmodel

import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.tmdbapp.ui.base.event.LoginFailedEvent
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.ui.cicerone.MainScreen
import com.mvgreen.tmdbapp.utils.onNext
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val useCase: AuthUseCase,
    private val router: Router
) : BaseViewModel() {

    var loginInProgress: Boolean = false

    fun onLogin(email: String, password: String) {
        loginInProgress = true
        useCase
            .login(email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    router.newRootScreen(MainScreen)
                },
                { e ->
                    events.onNext(LoginFailedEvent(e))
                }
            )
            .disposeOnViewModelDestroy()
    }

}