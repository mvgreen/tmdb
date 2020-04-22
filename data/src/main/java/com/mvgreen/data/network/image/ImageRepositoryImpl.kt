package com.mvgreen.data.network.image

import com.mvgreen.data.exception.ServerException
import com.mvgreen.data.network.image.api.ImageConfigurationApi
import com.mvgreen.domain.entity.ImageServiceConfiguration
import com.mvgreen.domain.repository.ImageRepository
import io.reactivex.Single
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageConfigurationApi: ImageConfigurationApi
) : ImageRepository {

    companion object {
        const val SIZE_INDEX_FROM_END = 1
    }

    override fun downloadConfiguration(): Single<ImageServiceConfiguration> {
        return imageConfigurationApi
            .getConfiguration()
            .map { response ->
                val logoSizes = response.images?.logoSizes
                if (logoSizes.isNullOrEmpty()) throw ServerException()

                var size = if (logoSizes.size > SIZE_INDEX_FROM_END) {
                    logoSizes[logoSizes.size - 1 - SIZE_INDEX_FROM_END]
                } else {
                    logoSizes.last()
                }

                ImageServiceConfiguration(
                    response.images.secureBaseUrl ?: throw ServerException(),
                    size
                )
            }
    }

}