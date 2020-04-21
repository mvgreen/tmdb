package com.mvgreen.tmdbapp.ui.launch.viewmodel

import android.util.Log
import com.mvgreen.domain.usecase.AuthUseCase
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.ui.base.event.LoadingCompletedEvent
import com.mvgreen.tmdbapp.ui.base.event.LoadingErrorEvent
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.utils.onNext
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val loadImageUseCase: LoadImageUseCase
) : BaseViewModel() {

    companion object {
        const val TAG = "LaunchViewModel"
    }

    fun onLoadConfig() {
        loadImageUseCase
            .downloadConfiguration()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    events.onNext(LoadingCompletedEvent)
                },
                { e ->
                    Log.e(TAG, e.message, e)
                    events.onNext(LoadingErrorEvent)
                }
            )
            .disposeOnViewModelDestroy()
    }

    fun hasUserData(): Boolean {
        return authUseCase.hasUserData()
    }

}