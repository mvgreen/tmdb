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
import com.mvgreen.tmdbapp.ui.rootscreen.fragment.BranchFragment
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.viewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*
import ru.terrakok.cicerone.Router

class DetailsFragment(private var movieId: Int) : BaseFragment(R.layout.fragment_details) {

    constructor() : this(ID_NONE)

    companion object {
        const val TAG = "DetailsFragment"
        const val MOVIE_ID = "MOVIE_ID"
        const val ID_NONE = -1

    }

    private lateinit var viewModel: DetailsViewModel
    private lateinit var router: Router

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        router = DI.filmsTabComponent.router()
        if (movieId == -1) {
            movieId = savedInstanceState?.getInt(MOVIE_ID, ID_NONE) ?: ID_NONE
        }
        setupViewModel()
        setupView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID, movieId)
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.detailsViewModel()
        })
    }

    private fun setupView() {
        description.movementMethod = ScrollingMovementMethod()
        button_back.setOnClickListener {
            router.exit()
        }
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
                            .make(
                                requireView(),
                                getString(R.string.check_connection),
                                Snackbar.LENGTH_SHORT
                            )
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
        val genres = movieData.genres.joinToString { item -> item.name }
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

        val imageLoader = ImageLoaderImpl(poster, R.drawable.cornered_orange, false) {}
        viewModel.onLoadImage(imageLoader)
    }
}