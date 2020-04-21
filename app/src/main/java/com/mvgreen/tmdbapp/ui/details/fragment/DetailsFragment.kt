package com.mvgreen.tmdbapp.ui.details.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.event.Event
import com.mvgreen.tmdbapp.ui.base.event.LoadCompletedEvent
import com.mvgreen.tmdbapp.ui.base.event.LoadErrorEvent
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.details.viewmodel.DetailsViewModel
import com.mvgreen.tmdbapp.utils.ImageLoaderImpl
import com.mvgreen.tmdbapp.utils.getViewModel
import com.mvgreen.tmdbapp.utils.observe
import com.mvgreen.tmdbapp.utils.viewModelFactory
import com.redmadrobot.lib.sd.LoadingStateDelegate
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
    private lateinit var loadingStateDelegate: LoadingStateDelegate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        router = DI.filmsTabComponent.router()
        if (movieId == -1) {
            movieId = savedInstanceState?.getInt(MOVIE_ID, ID_NONE) ?: ID_NONE
        }
        setupDelegate()
        setupViewModel()
        setupView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID, movieId)
    }

    private fun setupDelegate() {
        loadingStateDelegate = LoadingStateDelegate(content_screen, stubView = error_screen)
    }

    private fun setupViewModel() {
        viewModel = getViewModel(viewModelFactory {
            DI.appComponent.detailsViewModel()
        })
        observe(viewModel.events, ::onEvent)
    }

    private fun setupView() {
        description.movementMethod = ScrollingMovementMethod()
        button_back.setOnClickListener {
            router.exit()
        }
        button_retry.setOnClickListener {
            viewModel.onLoadMovieData(movieId)
        }

        viewModel.onLoadMovieData(movieId)
    }

    private fun bindData(movieData: MovieData) {
        val movieTitle = movieData.title ?: "-"
        val movieOriginalTitle = movieData.originalTitle ?: "-"
        val year = movieData.releaseDate?.year()?.get()?.toString() ?: "-"
        val genres = movieData.genres.joinToString { item -> item.name }
        val movieScore = movieData.averageVote?.toString() ?: "-"
        val voteCount = movieData.voteCount?.toString() ?: "-"
        val runtime = movieData.runtime?.toString() ?: "-"
        val overview = movieData.overview ?: getString(R.string.no_description)

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

    private fun onEvent(event: Event) {
        when (event) {
            is LoadCompletedEvent -> {
                bindData(viewModel.movieData)
                loadingStateDelegate.showContent()
            }

            is LoadErrorEvent -> {
                loadingStateDelegate.showStub()
            }

            else -> {
                Log.e(TAG, "Unexpected event $event")
            }
        }
    }
}