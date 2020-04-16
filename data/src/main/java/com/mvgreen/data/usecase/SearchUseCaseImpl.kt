package com.mvgreen.data.usecase

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mvgreen.data.datasource.SearchDataSourceFactory
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
import com.mvgreen.domain.usecase.SearchUseCase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository,
    private val genreStorage: GenreStorage
) : SearchUseCase {

    companion object {
        const val TAG = "SearchUseCaseImpl"
    }

    override fun initSearch(): Completable {
        return searchRepository
            .loadGenres()
            .map { result -> genreStorage.saveGenresMap(result) }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun search(
        query: String,
        compositeDisposable: CompositeDisposable
    ): RxPagedListBuilder<Int, MovieData> {
        val factory = SearchDataSourceFactory(
            query,
            searchRepository,
            compositeDisposable
        ) { e -> Log.e(TAG, e.message, e) }
        val config = PagedList.Config.Builder().setPrefetchDistance(5).build()
        return RxPagedListBuilder(factory, config)
    }

}
