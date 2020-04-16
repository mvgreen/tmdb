package com.mvgreen.tmdbapp.ui.films.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.adapter.PagedMoviesAdapter
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.branch_stub_films.*
import java.util.concurrent.TimeUnit

class FilmsBranchFragment : BaseFragment(R.layout.branch_stub_films) {

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
        val adapter = PagedMoviesAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.addItemDecoration(marginDecoration)

        DI.appComponent.searchUseCase().initSearch().subscribe().disposeOnViewModelDestroy()
        search_box.textChanges()
            .filter { it.length > 2 }
            .debounce(100, TimeUnit.MILLISECONDS)
            .switchMap { apiCall() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                adapter.submitList(list)
            }
            .disposeOnViewModelDestroy()
    }

    private fun apiCall(): ObservableSource<PagedList<MovieData>> {
        return DI.appComponent.searchUseCase().search("adventures", CompositeDisposable())
            .buildObservable()
    }
}