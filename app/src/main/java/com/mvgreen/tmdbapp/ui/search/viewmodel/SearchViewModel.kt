package com.mvgreen.tmdbapp.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData
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

    var savedListPosition: Int = 0

    var currentState: LoadingState? = LoadingState.CONTENT

    init {
        val (savedList, savedPosition, savedQuery) = searchUseCase.restoreListState()
        savedList?.let { livePagedList.onNext(it) }
        savedListPosition = savedPosition
        query = savedQuery
    }

    override fun onCleared() {
        val savedList = livePagedList.value
        searchUseCase.saveSearchState(savedList, savedListPosition, query)
        super.onCleared()
    }

    fun onSearch(
        query: String
    ) {
        searchUseCase
            .search(query, compositeDisposable)
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