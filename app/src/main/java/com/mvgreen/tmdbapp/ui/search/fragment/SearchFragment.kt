package com.mvgreen.tmdbapp.ui.search.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.adapter.PagedMoviesAdapter
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.search.viewmodel.SearchViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        const val KEY_QUERY = "KEY_QUERY"
        const val KEY_FIRST_ITEM = "KEY_FIRST_ITEM"
    }

    private lateinit var viewModel: SearchViewModel

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
        setupViewModel(savedInstanceState)
        setupView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_QUERY, viewModel.query)
        outState.putInt(
            KEY_FIRST_ITEM,
            (recycler_results.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        )
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
                val queryStr = query.toString()
                viewModel.query = queryStr
                viewModel.onSearch(queryStr)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                viewModel.list = list
                (recycler_results.adapter as PagedListAdapter<MovieData, *>).submitList(list)
            }
            .disposeOnDestroy()
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.searchViewModel()
        })
        if (savedInstanceState != null) {
            val query = savedInstanceState.getString(KEY_QUERY)
            if (query != null) {
                viewModel.query = query
            }
        }
    }

    private fun setupView(savedInstanceState: Bundle?) {
        val adapter = PagedMoviesAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        recycler_results.adapter = adapter
        recycler_results.layoutManager = layoutManager
        recycler_results.addItemDecoration(marginDecoration)
        if (savedInstanceState != null) {
            val firstItem = savedInstanceState.getInt(KEY_FIRST_ITEM, -1)
            if (firstItem != -1) {
                layoutManager.scrollToPosition(firstItem)
            }
        }
    }

    private fun restoreSearch() {
        input_search.setText(viewModel.query)
        (recycler_results.adapter as PagedMoviesAdapter).submitList(viewModel.list)
    }
}