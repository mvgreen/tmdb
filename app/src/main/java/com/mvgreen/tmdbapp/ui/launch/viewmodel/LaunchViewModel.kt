package com.mvgreen.tmdbapp.ui.launch.viewmodel

import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import io.reactivex.Completable
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
  private val loadImageUseCase: LoadImageUseCase
) : BaseViewModel() {

    fun onLoadConfig(): Completable {
        return loadImageUseCase
            .downloadConfiguration()
    }

}