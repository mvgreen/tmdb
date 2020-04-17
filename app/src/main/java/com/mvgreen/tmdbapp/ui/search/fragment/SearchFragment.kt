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
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.adapter.PagedMoviesAdapter
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.search.viewmodel.SearchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var stateDelegate: LoadingStateDelegate
    private lateinit var viewModel: SearchViewModel

    private val loadImageUseCase: LoadImageUseCase = DI.appComponent.loadImageUseCase()

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
            .switchMap { query ->
                onLoadingStarted()
                val queryStr = query.toString()
                viewModel.query = queryStr
                viewModel.onSearch(queryStr)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    onLoaded()
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
        val adapter = PagedMoviesAdapter(loadImageUseCase)
        val layoutManager = LinearLayoutManager(requireContext())
        recycler_results.adapter = adapter
        recycler_results.layoutManager = layoutManager
        recycler_results.addItemDecoration(marginDecoration)
    }

    private fun setupDelegator() {
        stateDelegate = LoadingStateDelegate(recycler_results, loading_screen, zero_screen)
    }

    private fun restoreSearch() {
        input_search.setText(viewModel.query)
        (recycler_results.adapter as PagedMoviesAdapter).submitList(viewModel.list)
    }

    private fun onLoadingStarted() {
        requireActivity().runOnUiThread {
            stateDelegate.showLoading()
        }
    }

    private fun onLoaded() {
        requireActivity().runOnUiThread {
            stateDelegate.showContent()
        }
    }

    // TODO случай с отсутствием интернета
    private fun onContentEmpty() {
    }
}