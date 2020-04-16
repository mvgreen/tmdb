package com.mvgreen.domain.usecase

import androidx.paging.RxPagedListBuilder
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable


interface SearchUseCase {

    fun initSearch(): Completable

    fun search(
        query: String,
        compositeDisposable: CompositeDisposable
    ): RxPagedListBuilder<Int, MovieData>

    fun initImageLoader(imageLoader: ImageLoader, movieData: MovieData)

}