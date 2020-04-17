package com.mvgreen.tmdbapp.ui.search.viewmodel

import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : BaseViewModel() {

    var query = ""

    var list : PagedList<MovieData>? = null

    fun onSearch(query: String) : Observable<PagedList<MovieData>> {
        return searchUseCase
            .search(query, compositeDisposable)
    }

}