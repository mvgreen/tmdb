package com.mvgreen.domain.usecase

import androidx.paging.DataSource
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Completable


interface SearchUseCase {

    fun initSearch(): Completable

    fun search(query: String): DataSource<Int, MovieData>

}