package com.mvgreen.tmdbapp.ui.details.viewmodel

import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.usecase.DetailsUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase
): BaseViewModel() {

    var movieData: MovieData? = null

    fun onLoadMovieData(id: Int) : Single<MovieData> {
        return detailsUseCase
            .loadMovie(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { result ->
                movieData = result
            }
    }

}