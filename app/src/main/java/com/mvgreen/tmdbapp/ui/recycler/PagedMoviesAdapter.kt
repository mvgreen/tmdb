package com.mvgreen.tmdbapp.ui.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.domain.bean.ListMode
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_GRID
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_LINEAR
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.entity.SearchState
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.cicerone.DetailsScreen
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import kotlinx.android.synthetic.main.item_recycler_linear.view.*
import ru.terrakok.cicerone.Router

class PagedMoviesAdapter(
    private val router: Router,
    onSearchResultCallback: (searchState: SearchState) -> Unit
) : RecyclerView.Adapter<Holder>() {

    companion object {
        const val TAG = "PagedMoviesAdapter"
    }

    private lateinit var listMode: ListMode

    private val imageUseCase: LoadImageUseCase = DI.appComponent.loadImageUseCase()

    private val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
            onSearchResultCallback.invoke(SearchState.CONTENT_READY)
        }

        override fun onRemoved(position: Int, count: Int) {
            if (position == 0 && count == diffCallback.itemCount) {
                onSearchResultCallback.invoke(SearchState.EMPTY_RESPONSE)
            }
            notifyItemRangeRemoved(position, count)
        }
    }

    val diffCallback = object : AsyncPagedListDiffer<MovieData>(
        listUpdateCallback,
        AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<MovieData>() {
            override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
                return oldItem == newItem
            }
        }).build()
    ) {}

    fun submitList(newPagedList: PagedList<MovieData>?) {
        diffCallback.submitList(newPagedList)
        notifyDataSetChanged()
    }

    fun changeListMode(newListMode: ListMode) {
        listMode = newListMode
        this.notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return listMode.modeId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val itemId = when (viewType) {
            LIST_MODE_LINEAR -> R.layout.item_recycler_linear
            LIST_MODE_GRID -> R.layout.item_recycler_grid
            else -> throw IllegalArgumentException()
        }
        return Holder(inflater.inflate(itemId, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movieData = diffCallback.getItem(position) ?: return
        val movieTitle = movieData.title ?: "-"
        val movieOriginalTitle = movieData.originalTitle ?: "-"
        val year = movieData.releaseDate?.year()?.get()?.toString() ?: "-"
        val genres = movieData.genres.joinToString { item -> item.name }
        val movieScore = movieData.averageVote?.toString() ?: "-"
        val voteCount = movieData.voteCount?.toString() ?: "-"
        with(holder.itemView) {
            title.text = movieTitle
            subtitle.text = resources.getString(R.string.title_template, movieOriginalTitle, year)
            genre.text = genres
            score.text = movieScore
            vote_count.text = voteCount
            loadImage(poster, movieData)
        }
        holder.itemView.setOnClickListener {
            router.navigateTo(DetailsScreen(movieData.id))
        }
    }

    override fun getItemCount(): Int {
        return diffCallback.itemCount
    }

    private fun loadImage(item: View, movie: MovieData?) {
        val imageLoader = ImageLoaderImpl(
            item.poster,
            R.drawable.cornered_orange,
            false
        ) {
            Log.e(TAG, "Could not load poster image")
        }
        imageUseCase.initImageLoader(imageLoader, movie)
        imageLoader.loadImage()
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)