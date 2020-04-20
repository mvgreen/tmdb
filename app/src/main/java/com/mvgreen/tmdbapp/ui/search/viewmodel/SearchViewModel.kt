package com.mvgreen.tmdbapp.ui.search.viewmodel

import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.lib.sd.LoadingStateDelegate.LoadingState
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : BaseViewModel() {

    var query = ""

    var list: PagedList<MovieData>? = null

    var currentState: LoadingState? = LoadingState.CONTENT

    fun onSearch(
        query: String,
        searchStateCallback: (state: SearchState, currentQuery: String) -> Unit
    ): Observable<PagedList<MovieData>> {
        return searchUseCase
            .search(query, compositeDisposable, searchStateCallback)
    }

}