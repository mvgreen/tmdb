package com.mvgreen.tmdbapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import kotlinx.android.synthetic.main.item_recycler_linear.view.*

class PagedMoviesAdapter : PagedListAdapter<MovieData, RecyclerView.ViewHolder>(diffCallback) {

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
        val movieData = getItem(position)
        val movieTitle = movieData?.title ?: "-"
        val movieOriginalTitle = movieData?.originalTitle ?: "-"
        val year = movieData?.releaseDate?.year()?.get()?.toString() ?: "-"
        val genres = movieData?.genres?.joinToString()
        val movieScore = movieData?.averageVote?.toString() ?: "-"
        val voteCount = movieData?.voteCount?.toString() ?: "-"
        val runtime = movieData?.runtime?.toString() ?: "-"
        with(holder.itemView) {
            title.text = resources.getString(R.string.title_template, movieTitle, year)
            subtitle.text = resources.getString(R.string.title_template, movieOriginalTitle, year)
            genre.text = genres
            score.text = movieScore
            vote_count.text = voteCount
            length.text = runtime
        }
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)