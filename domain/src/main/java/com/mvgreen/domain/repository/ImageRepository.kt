package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.ImageServiceConfiguration

interface ImageRepository {

    fun downloadConfiguration(): ImageServiceConfiguration

}