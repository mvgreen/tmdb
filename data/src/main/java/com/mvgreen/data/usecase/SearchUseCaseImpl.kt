package com.mvgreen.data.usecase

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mvgreen.data.datasource.SearchDataSourceFactory
import com.mvgreen.domain.bean.ListMode
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.AppPreferencesStorage
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
import com.mvgreen.domain.repository.SearchStorage
import com.mvgreen.domain.usecase.SearchUseCase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository,
    private val genreStorage: GenreStorage,
    private val searchStorage: SearchStorage,
    private val appPreferencesStorage: AppPreferencesStorage
) : SearchUseCase {

    companion object {
        const val TAG = "SearchUseCaseImpl"
        const val PREFETCH_DISTANCE = 10
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
    ): Observable<PagedList<MovieData>> {
        val factory = SearchDataSourceFactory(
            query,
            searchRepository,
            compositeDisposable
        ) { e -> Log.e(TAG, e.message, e) }
        val config = PagedList.Config
            .Builder()
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
        return RxPagedListBuilder(factory, config).buildObservable()
    }

    override fun saveSearchState(
        pagedList: PagedList<MovieData>?,
        listPosition: Int,
        query: String
    ) {
        searchStorage.savedList = pagedList
        searchStorage.savedListPosition = listPosition
        searchStorage.savedQuery = query
    }

    override fun restoreListState(): Triple<PagedList<MovieData>?, Int, String> {
        return Triple(
            searchStorage.savedList,
            searchStorage.savedListPosition,
            searchStorage.savedQuery
        )
    }

    override fun setListMode(listMode: Int) {
        appPreferencesStorage.setListMode(listMode)
    }

    override fun initListMode(listMode: ListMode): ListMode {
        val modeId = appPreferencesStorage.getListMode()
        listMode.modeId = modeId
        return listMode
    }

}
