package com.mvgreen.data.datasource

import androidx.paging.DataSource
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(
    private val query: String,
    private val searchRepository: SearchRepository,
    private var compositeDisposable: CompositeDisposable,
    private val onErrorCallback: (e: Throwable) -> Unit
) : DataSource.Factory<Int, MovieData>() {

    override fun create(): DataSource<Int, MovieData> {
        return SearchDataSource(
            query,
            searchRepository,
            compositeDisposable,
            onErrorCallback
        )
    }

}
