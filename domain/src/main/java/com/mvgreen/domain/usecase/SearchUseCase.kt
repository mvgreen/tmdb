package com.mvgreen.domain.usecase

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


interface SearchUseCase {

    fun initSearch(): Completable

    fun search(
        query: String,
        compositeDisposable: CompositeDisposable
    ): Observable<PagedList<MovieData>>

    fun saveSearchState(pagedList: PagedList<MovieData>?, listPosition: Int, query: String)

    fun restoreListState(): Triple<PagedList<MovieData>?, Int, String>

}