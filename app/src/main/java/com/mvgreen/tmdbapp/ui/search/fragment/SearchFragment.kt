package com.mvgreen.tmdbapp.ui.search.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
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
        setupViewModel()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        input_search.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(input_search, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.searchViewModel()
        })
    }

    private fun setupView() {
        val adapter = PagedMoviesAdapter()
        recycler_results.adapter = adapter
        recycler_results.layoutManager = LinearLayoutManager(requireContext())
        recycler_results.addItemDecoration(marginDecoration)

        input_search.textChanges()
            .filter { it.length > 2 }
            .debounce(100, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                viewModel.onSearch(query.toString())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                adapter.submitList(list)
            }
            .disposeOnViewModelDestroy()
    }

}