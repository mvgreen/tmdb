package com.mvgreen.domain.usecase

import androidx.paging.DataSource
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable


interface SearchUseCase {

    fun initSearch(): Completable

    fun search(
        query: String,
        compositeDisposable: CompositeDisposable
    ): DataSource.Factory<Int, MovieData>

}