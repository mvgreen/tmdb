package com.mvgreen.data.datasource

import androidx.paging.PageKeyedDataSource
import com.mvgreen.domain.entity.MovieContainer
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchDataSource(
    private val query: String,
    private val searchRepository: SearchRepository,
    private var compositeDisposable: CompositeDisposable,
    private val onErrorCallback: (e: Throwable) -> Unit,
    private val searchStateCallback: (state: SearchState, currentQuery: String) -> Unit
) : PageKeyedDataSource<Int, MovieData>() {

    private var pagesTotal: Int = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieData>
    ) {
        searchRepository
            .search(query, 1)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    pagesTotal = result.pagesTotal
                    val searchState = if (pagesTotal == 0) {
                        SearchState.EMPTY_RESPONSE
                    } else {
                        SearchState.CONTENT_READY
                    }
                    searchStateCallback.invoke(searchState, query)
                    callOnResult(result) { page, nextKey ->
                        callback.onResult(page, null, nextKey)
                    }
                },
                { e ->
                    searchStateCallback.invoke(SearchState.ERROR, query)
                    onErrorCallback.invoke(e)
                }
            )
            .disposeOnDestroy()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        searchRepository
            .search(query, params.key)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    callOnResult(result) { page, nextKey ->
                        callback.onResult(page, nextKey)
                    }
                },
                { e ->
                    searchStateCallback.invoke(SearchState.ERROR, query)
                    onErrorCallback.invoke(e)
                }
            )
            .disposeOnDestroy()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        throw NotImplementedError()
    }

    private fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }

    private inline fun callOnResult(
        result: MovieContainer,
        toCall: (List<MovieData>, Int?) -> Unit
    ) {
        val nextPage =
            if (pagesTotal == result.currentPage) null else result.currentPage + 1
        toCall.invoke(result.movies, nextPage)
    }

}