package com.mvgreen.tmdbapp.ui.details.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.details.viewmodel.DetailsViewModel
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment(private val movieId: Int) : BaseFragment(R.layout.fragment_details) {

    companion object {
        const val TAG = "DetailsFragment"
    }

    private lateinit var viewModel: DetailsViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupView()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.detailsViewModel()
        })
    }

    private fun setupView() {
        description.movementMethod = ScrollingMovementMethod()
        val movie = viewModel.movieData
        if (movie == null) {
            viewModel
                .onLoadMovieData(movieId)
                .subscribe(
                    { result ->
                        bindData(result)
                    },
                    { e ->
                        Log.e(TAG, e.message, e)
                        Snackbar
                            .make(container, getString(R.string.check_connection), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                )
                .disposeOnDestroy()
        } else {
            bindData(movie)
        }
    }

    private fun bindData(movieData: MovieData) {
        val movieTitle = movieData.title ?: "-"
        val movieOriginalTitle = movieData.originalTitle ?: "-"
        val year = movieData.releaseDate?.year()?.get()?.toString() ?: "-"
        val genres = movieData.genres.joinToString()
        val movieScore = movieData.averageVote?.toString() ?: "-"
        val voteCount = movieData.voteCount?.toString() ?: "-"
        val runtime = movieData.runtime?.toString() ?: "-"
        val overview = movieData.overview?.toString() ?: getString(R.string.no_description)

        title.text = movieTitle
        title_original.text = resources.getString(R.string.title_template, movieOriginalTitle, year)
        genre.text = genres
        score.text = movieScore
        vote_count.text = voteCount
        length.text = runtime
        description.text = overview
    }
}