package com.mvgreen.tmdbapp.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.mvgreen.domain.bean.ListMode
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.usecase.SearchUseCase
import com.mvgreen.tmdbapp.ui.base.viewmodel.BaseViewModel
import com.mvgreen.tmdbapp.ui.recycler.ListModeImpl
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

    // Используем реализацию вместо интерфейса для доступности метода создания менеджера
    private var listMode: ListModeImpl? = null

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

    fun getListMode(): ListModeImpl {
        val currentListMode = listMode ?: searchUseCase.initListMode(ListModeImpl())
        listMode = currentListMode as ListModeImpl
        return currentListMode
    }

    fun changeListMode(): ListModeImpl {
        val currentList = getListMode()
        val nextId = currentList.nextModeId()
        searchUseCase.setListMode(nextId)
        // Производим повторную инициализацию
        listMode = null
        return getListMode()
    }

    fun onStop() {
        savedListPosition = listMode?.listPosition ?: 0
        listMode?.resetLayoutManager()
    }

    fun resetState() {
        query = ""
        livePagedList.value = null
        savedListPosition = 0
        currentState = LoadingState.CONTENT
        listMode = null
    }

}