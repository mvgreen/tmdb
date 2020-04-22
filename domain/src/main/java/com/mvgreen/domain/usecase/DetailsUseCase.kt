package com.mvgreen.domain.usecase

import com.mvgreen.domain.entity.MovieData
import io.reactivex.Single

interface DetailsUseCase {

    fun loadMovie(id: Int) : Single<MovieData>

}