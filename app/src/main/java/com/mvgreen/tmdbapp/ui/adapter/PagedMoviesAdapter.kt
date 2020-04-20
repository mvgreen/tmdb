package com.mvgreen.tmdbapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.cicerone.DetailsScreen
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import kotlinx.android.synthetic.main.item_recycler_linear.view.*
import ru.terrakok.cicerone.Router

class PagedMoviesAdapter(
    private val imageUseCase: LoadImageUseCase,
    private val router: Router
) : PagedListAdapter<MovieData, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        const val TAG = "PagedMoviesAdapter"

        val diffCallback = object : DiffUtil.ItemCallback<MovieData>() {
            override fun areItemsTheSame(oldData: MovieData, newData: MovieData): Boolean {
                return oldData == newData
            }

            override fun areContentsTheSame(oldData: MovieData, newData: MovieData): Boolean {
                return oldData == newData
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.item_recycler_linear, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieData = getItem(position) ?: return
        val movieTitle = movieData.title ?: "-"
        val movieOriginalTitle = movieData.originalTitle ?: "-"
        val year = movieData.releaseDate?.year()?.get()?.toString() ?: "-"
        val genres = movieData.genres.joinToString { item -> item.name }
        val movieScore = movieData.averageVote?.toString() ?: "-"
        val voteCount = movieData.voteCount?.toString() ?: "-"
        val runtime = movieData.runtime?.toString() ?: "-"
        with(holder.itemView) {
            title.text = movieTitle
            subtitle.text = resources.getString(R.string.title_template, movieOriginalTitle, year)
            genre.text = genres
            score.text = movieScore
            vote_count.text = voteCount
            length.text = resources.getString(R.string.minutes, runtime)
            loadImage(poster, movieData)
        }
        holder.itemView.setOnClickListener {
            router.navigateTo(DetailsScreen(movieData.id))
        }
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