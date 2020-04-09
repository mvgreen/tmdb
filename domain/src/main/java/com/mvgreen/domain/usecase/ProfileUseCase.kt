package com.mvgreen.domain.usecase

import com.mvgreen.domain.bean.ImageLoader
import io.reactivex.Single

interface ProfileUseCase {

    fun getAvatarLoader(imageLoader: ImageLoader): Single<ImageLoader>

}