package com.mvgreen.tmdbapp.ui.films.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.branch_stub_films.*
import kotlinx.android.synthetic.main.simple_item.view.*
import java.util.concurrent.TimeUnit


class FilmsBranchFragment : BaseFragment(R.layout.branch_stub_films) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = MockAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

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

    private fun apiCall(): ObservableSource<PagedList<String>>? {
        return RxPagedListBuilder(
            MockDataSourceFactory(),
            PagedList.Config.Builder().setPrefetchDistance(1).build()
        ).buildObservable()
    }
}

class MockDataSource : PageKeyedDataSource<Int, String>() {

    private val list = mutableListOf(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    )

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, String>
    ) {
        callback.onResult(list.subList(0, 8), 0, 8, null, 8)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, String>) {
        val start = params.key
        var end = start + 6
        if (end >= 26) end = 26
        val next = if (end >= 26) null else end + 1
        callback.onResult(list.subList(start, end), next)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, String>) {
        val start = params.key
        val end = start - 6
        val next = if (end == 0) null else end - 1
        callback.onResult(list.subList(start, end), next)
    }

}

class MockDataSourceFactory : DataSource.Factory<Int, String>() {
    override fun create(): DataSource<Int, String> {
        return MockDataSource()
    }

}


class MockAdapter :
    PagedListAdapter<String, RecyclerView.ViewHolder>(userDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.simple_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text_holder.text = getItem(position)
    }

    companion object {
        val userDiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)