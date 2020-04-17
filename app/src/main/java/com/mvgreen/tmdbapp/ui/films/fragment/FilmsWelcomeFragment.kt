package com.mvgreen.tmdbapp.ui.films.fragment

import android.os.Bundle
import com.mvgreen.domain.usecase.LoadImageUseCase
import com.mvgreen.tmdbapp.R
import com.mvgreen.tmdbapp.internal.di.DI
import com.mvgreen.tmdbapp.ui.base.fragment.BaseFragment
import com.mvgreen.tmdbapp.ui.cicerone.SearchScreen
import kotlinx.android.synthetic.main.fragment_films_welcome.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class FilmsWelcomeFragment : BaseFragment(R.layout.fragment_films_welcome) {

    private lateinit var filmsRouter: Router

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filmsRouter = DI.filmsTabComponent.router()
        search_button.setOnClickListener {
            filmsRouter.navigateTo(SearchScreen)
        }
    }

}