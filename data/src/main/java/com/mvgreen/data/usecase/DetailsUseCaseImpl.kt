package com.mvgreen.data.usecase

import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.SearchRepository
import com.mvgreen.domain.usecase.DetailsUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : DetailsUseCase {

    override fun loadMovie(id: Int): Single<MovieData> {
        return searchRepository
            .getMovieDetails(id)
            .subscribeOn(Schedulers.io())
    }

}