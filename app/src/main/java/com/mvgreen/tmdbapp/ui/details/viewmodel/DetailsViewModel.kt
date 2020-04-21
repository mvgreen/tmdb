package com.mvgreen.tmdbapp.ui.details.viewmodel

import android.util.Log
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.usecase.DetailsUseCase
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.ui.base.event.LoadCompletedEvent
import com.mvgreen.tmdbapp.ui.base.event.LoadErrorEvent
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.utils.onNext
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase,
    private val imageUseCase: LoadImageUseCase
): BaseViewModel() {

    companion object {
        const val TAG = "DetailsViewModel"
    }

    lateinit var movieData: MovieData

    fun onLoadMovieData(id: Int) {
        if (::movieData.isInitialized) {
            events.onNext(LoadCompletedEvent)
            return
        }

        detailsUseCase
            .loadMovie(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    movieData = result
                    events.onNext(LoadCompletedEvent)
                },
                { e ->
                    Log.e(TAG, e.message, e)
                    events.onNext(LoadErrorEvent)
                }

            )
            .disposeOnViewModelDestroy()
    }

    fun onLoadImage(imageLoader: ImageLoader) {
        imageUseCase.initImageLoader(imageLoader, movieData)
        imageLoader.loadImage()
    }

}