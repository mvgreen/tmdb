package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.ImageServiceConfiguration

interface ImageConfigStorage {

    fun setConfiguration(configuration: ImageServiceConfiguration)

    fun getUrl(): String

    fun getPath(): String

} 