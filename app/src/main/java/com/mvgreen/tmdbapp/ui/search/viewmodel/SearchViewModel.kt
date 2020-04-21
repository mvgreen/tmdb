package com.mvgreen.tmdbapp.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.utils.onNext
import com.redmadrobot.lib.sd.LoadingStateDelegate.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : BaseViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
    }

    var query = ""

    var livePagedList: MutableLiveData<PagedList<MovieData>> =
        MutableLiveData<PagedList<MovieData>>()

    var currentState: LoadingState? = LoadingState.CONTENT

    fun onSearch(
        query: String,
        searchStateCallback: (state: SearchState, currentQuery: String) -> Unit
    ) {
        searchUseCase
            .search(query, compositeDisposable, searchStateCallback)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newList ->
                    livePagedList.onNext(newList)
                },
                { e ->
                    Log.e(TAG, e.message, e)
                }
            )
            .disposeOnViewModelDestroy()
    }

}