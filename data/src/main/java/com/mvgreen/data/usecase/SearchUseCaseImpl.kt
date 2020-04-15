package com.mvgreen.data.usecase

import android.util.Log
import androidx.paging.DataSource
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
import com.mvgreen.domain.usecase.SearchUseCase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    override fun search(query: String): DataSource<Int, MovieData> {
        return SearchDataSource(query, searchRepository) { e -> Log.e(TAG, e.message, e) }
    }

}
