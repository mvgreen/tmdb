package com.mvgreen.domain.usecase

import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Completable

interface LoadImageUseCase {

    fun downloadConfiguration(): Completable

    fun initAvatarLoader(imageLoader: ImageLoader)

    fun initImageLoader(imageLoader: ImageLoader, movieData: MovieData?)

}