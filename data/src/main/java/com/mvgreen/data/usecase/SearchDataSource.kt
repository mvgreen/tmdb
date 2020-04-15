package com.mvgreen.data.usecase

import androidx.paging.PageKeyedDataSource
import com.mvgreen.domain.entity.MovieContainer
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchDataSource(
    private val query: String,
    private val searchRepository: SearchRepository,
    private val onErrorCallback: (e: Throwable) -> Unit
) : PageKeyedDataSource<Int, MovieData>(), Disposable {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    private var pagesTotal: Int = 0

    @Volatile
    private var disposed: Boolean = false

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
                    callOnResult(result) { page, nextKey ->
                        callback.onResult(page, null, nextKey)
                    }
                },
                { e ->
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
                    onErrorCallback.invoke(e)
                }
            )
            .disposeOnDestroy()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        throw NotImplementedError()
    }


    override fun isDisposed(): Boolean {
        return disposed
    }

    override fun dispose() {
        if (disposed) {
            return
        }
        var disposable: CompositeDisposable
        synchronized(this) {
            if (disposed) {
                return
            }
            disposed = true
            disposable = compositeDisposable ?: return
            compositeDisposable = null
        }

        disposable.clear()
    }

    private fun Disposable.disposeOnDestroy() {
        if (!disposed) {
            synchronized(this) {
                if (!disposed) {
                    val container = compositeDisposable ?: CompositeDisposable()
                    compositeDisposable = container
                    container.add(this)
                }
            }
        }
        this.dispose()
    }

    private inline fun callOnResult(
        result: MovieContainer,
        callback: (List<MovieData>, Int?) -> Unit
    ) {
        val nextPage =
            if (pagesTotal == result.currentPage) null else result.currentPage + 1
        callback.invoke(result.movies, nextPage)
    }

}