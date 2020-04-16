package com.mvgreen.domain.usecase

import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData

interface LoadImageUseCase {

    fun downloadConfiguration()

    fun initAvatarLoader(imageLoader: ImageLoader)

    fun initListImageLoader(imageLoader: ImageLoader, movieData: MovieData)

}