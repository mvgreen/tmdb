package com.mvgreen.data.usecase

import com.mvgreen.data.network.factory.NetworkConstants
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.ImageConfigStorage
import com.mvgreen.domain.repository.ImageRepository
import com.mvgreen.domain.repository.UserDataStorage
import com.mvgreen.domain.usecase.LoadImageUseCase
import javax.inject.Inject

// TODO добавить в даггер и перенести инициализацию аватарки из другого юзкейса
class LoadImageUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository,
    private val imageConfigStorage: ImageConfigStorage,
    private val userDataStorage: UserDataStorage
) : LoadImageUseCase {

    override fun downloadConfiguration() {
        val config = imageRepository.downloadConfiguration()
        imageConfigStorage.setConfiguration(config)
    }

    override fun initAvatarLoader(imageLoader: ImageLoader) {
        val profileData = userDataStorage.getProfileDataOrDie()

        imageLoader.url = NetworkConstants.Gravatar.url
        imageLoader.path = profileData.avatarHash
        imageLoader.sizeParam = NetworkConstants.Gravatar.sizeModifier
    }

    override fun initListImageLoader(imageLoader: ImageLoader, movieData: MovieData) {
        if (movieData.posterLink == null) {
            return
        }

        imageLoader.url = imageConfigStorage.getUrl()
        imageLoader.path = movieData.posterLink
    }

}