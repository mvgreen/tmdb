package com.mvgreen.tmdbapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.ui.films.fragment.Holder
import kotlinx.android.synthetic.main.simple_item.view.*

class PagedMoviesAdapter : PagedListAdapter<MovieData, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
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
        return Holder(inflater.inflate(R.layout.simple_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text_holder.text = getItem(position)!!.originalTitle
    }

}