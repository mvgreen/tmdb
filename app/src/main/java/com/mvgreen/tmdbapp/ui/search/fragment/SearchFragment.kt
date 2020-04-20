package com.mvgreen.tmdbapp.ui.search.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.adapter.PagedMoviesAdapter
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.delegator.EmptyResponseState
import com.mvgreen.tmdbapp.ui.delegator.ErrorState
import com.mvgreen.tmdbapp.ui.delegator.HideAllState
import com.mvgreen.tmdbapp.ui.search.viewmodel.SearchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
import com.redmadrobot.lib.sd.LoadingStateDelegate.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var stateDelegate: LoadingStateDelegate
    private lateinit var viewModel: SearchViewModel

    private val loadImageUseCase: LoadImageUseCase = DI.appComponent.loadImageUseCase()
    private val filmsRouter: Router = DI.filmsTabComponent.router()

    private val marginDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = resources.getDimension(R.dimen.recycler_margin).toInt()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupView()
        setupDelegator()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onResume() {
        super.onResume()
        if (viewModel.list.isNullOrEmpty()) {
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
            .switchMap { query ->
                onLoadingStarted()
                val queryStr = query.toString()
                viewModel.query = queryStr
                viewModel.onSearch(queryStr, ::onSearchStateChanged)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    viewModel.list = list
                    (recycler_results.adapter as PagedListAdapter<MovieData, *>).submitList(list)
                },
                { e ->
                    Log.e(TAG, e.message, e)
                }
            )
            .disposeOnDestroy()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.searchViewModel()
        })
    }

    private fun setupView() {
        val adapter = PagedMoviesAdapter(loadImageUseCase, filmsRouter)
        val layoutManager = LinearLayoutManager(requireContext())
        recycler_results.adapter = adapter
        recycler_results.layoutManager = layoutManager
        recycler_results.addItemDecoration(marginDecoration)
        button_cancel.setOnClickListener {
            input_search.setText("")
        }
        zero_screen.targetStates = listOf(EmptyResponseState, ErrorState)
        empty_screen.targetStates = listOf(HideAllState)

        input_search.setOnEditorActionListener { _, _, _ ->
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
        (recycler_results.adapter as PagedMoviesAdapter).submitList(viewModel.list)
    }

    private fun onSearchStateChanged(searchState: SearchState, query: String) {
        if (!isResumed || query != input_search?.text.toString()) {
            return
        }
        when (searchState) {
            SearchState.CONTENT_READY -> onLoaded()
            SearchState.ERROR -> onNetworkError()
            SearchState.EMPTY_RESPONSE -> onContentEmpty()
        }
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