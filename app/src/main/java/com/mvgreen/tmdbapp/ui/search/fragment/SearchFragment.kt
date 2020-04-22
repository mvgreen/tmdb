package com.mvgreen.tmdbapp.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_GRID
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_LINEAR
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.delegator.EmptyResponseState
import com.mvgreen.tmdbapp.ui.delegator.ErrorState
import com.mvgreen.tmdbapp.ui.delegator.HideAllState
import com.mvgreen.tmdbapp.ui.recycler.ListModeImpl
import com.mvgreen.tmdbapp.ui.recycler.PagedMoviesAdapter
import com.mvgreen.tmdbapp.ui.search.viewmodel.SearchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.observe
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
import com.redmadrobot.lib.sd.LoadingStateDelegate.LoadingState
import kotlinx.android.synthetic.main.fragment_search.*
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"

        const val SPAN_COUNT = 2
    }

    private lateinit var stateDelegate: LoadingStateDelegate
    private lateinit var viewModel: SearchViewModel
    private lateinit var listMode: ListModeImpl
    private lateinit var currentItemDecoration: RecyclerView.ItemDecoration

    private val filmsRouter: Router = DI.filmsTabComponent.router()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupView()
        setupDelegator()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.livePagedList.value == null) {
            input_search.requestFocus()

            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input_search, InputMethodManager.SHOW_IMPLICIT)
        } else {
            restoreSearch()
        }

        input_search.textChanges()
            // Первое значение - либо пустая строка, либо восстановленный с прошлого входа запрос
            // В любом случае повторное обращение к серверу не нужно
            .skipInitialValue()
            .debounce(200, TimeUnit.MILLISECONDS)
            .map { query ->
                if (query.isEmpty()) {
                    onHideContent()
                }
                query
            }
            .filter { query -> query.isNotEmpty() }
            .subscribe(
                { query ->
                    performSearch(query.toString())
                },
                { e ->
                    Log.e(TAG, e.message, e)
                }
            )
            .disposeOnDestroy()
    }

    override fun onStop() {
        viewModel.savedListPosition = listMode.listPosition
        super.onStop()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.searchViewModel()
        })
        observe(viewModel.livePagedList, ::onListUpdate)
    }

    private fun setupView() {
        listMode = ListModeImpl()
        viewModel.initListMode(listMode)
        val layoutManager = restoreListMode(listMode)
        layoutManager.scrollToPosition(viewModel.savedListPosition)

        val adapter = PagedMoviesAdapter(filmsRouter, listMode, ::onSearchStateChanged)

        recycler_results.adapter = adapter
        recycler_results.layoutManager = layoutManager
        currentItemDecoration = listMode.getMarginDecoration(resources)
        recycler_results.addItemDecoration(currentItemDecoration)
        button_cancel.setOnClickListener {
            input_search.setText("")
        }
        button_change_mode.setOnClickListener {
            changeListMode()
        }

        zero_screen.targetStates = listOf(EmptyResponseState, ErrorState)
        empty_screen.targetStates = listOf(HideAllState)

        input_search.setOnEditorActionListener { _, _, _ ->
            performSearch(input_search.text.toString())

            // клавиатура может не успеть исчезнуть после закрытия активити
            activity?.let { activity ->
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            }
            true
        }
    }

    private fun setupDelegator() {
        stateDelegate = LoadingStateDelegate(
            listOf(recycler_results),
            listOf(loading_screen),
            listOf(zero_screen, empty_screen)
        )
        stateDelegate.currentState = viewModel.currentState
    }

    private fun restoreSearch() {
        input_search.setText(viewModel.query)
    }

    private fun restoreListMode(listMode: ListModeImpl): RecyclerView.LayoutManager {
        listMode.layoutManager = when (listMode.modeId) {
            LIST_MODE_LINEAR -> LinearLayoutManager(requireContext())
            LIST_MODE_GRID -> GridLayoutManager(requireContext(), SPAN_COUNT)
            else -> throw IllegalArgumentException()
        }
        return listMode.layoutManager
    }

    private fun changeListMode() {
        val newModeId = listMode.nextModeId()

        val newListMode = ListModeImpl()
        newListMode.modeId = newModeId
        newListMode.layoutManager = restoreListMode(newListMode)

        val newItemDecoration = newListMode.getMarginDecoration(resources)

        val previousPosition = listMode.listPosition

        recycler_results.layoutManager = newListMode.layoutManager

        recycler_results.removeItemDecoration(currentItemDecoration)
        recycler_results.addItemDecoration(newItemDecoration)

        (recycler_results.adapter as PagedMoviesAdapter).changeListMode(newListMode)

        newListMode.layoutManager.scrollToPosition(previousPosition)
        recycler_results.invalidate()

        listMode = newListMode
        currentItemDecoration = newItemDecoration
        viewModel.setListMode(listMode.modeId)
    }

    private fun performSearch(query: String) {
        onLoadingStarted()
        viewModel.query = query
        viewModel.onSearch(query)
    }

    private fun onSearchStateChanged(searchState: SearchState) {
        if (!isResumed) {
            return
        }
        when (searchState) {
            SearchState.CONTENT_READY -> onLoaded()
            SearchState.ERROR -> onNetworkError()
            SearchState.EMPTY_RESPONSE -> onContentEmpty()
        }
    }

    private fun onListUpdate(newList: PagedList<MovieData>) {
        (recycler_results.adapter as PagedMoviesAdapter).submitList(newList)
    }

    private fun onLoadingStarted() {
        requireActivity().runOnUiThread {
            stateDelegate.showLoading()
            viewModel.currentState = LoadingState.LOADING
        }
    }

    private fun onLoaded() {
        requireActivity().runOnUiThread {
            stateDelegate.showContent()
            viewModel.currentState = LoadingState.CONTENT
        }
    }

    private fun onNetworkError() {
        requireActivity().runOnUiThread {
            stateDelegate.showStub(ErrorState)
            viewModel.currentState = LoadingState.STUB
        }
    }

    private fun onContentEmpty() {
        requireActivity().runOnUiThread {
            stateDelegate.showStub(EmptyResponseState)
            viewModel.currentState = LoadingState.STUB
        }
    }

    private fun onHideContent() {
        requireActivity().runOnUiThread {
            stateDelegate.showStub(HideAllState)
            viewModel.currentState = null
        }
    }
}