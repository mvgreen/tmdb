package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.ImageServiceConfiguration
import io.reactivex.Single

interface ImageRepository {

    fun downloadConfiguration(): Single<ImageServiceConfiguration>

}