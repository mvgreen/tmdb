package com.mvgreen.domain.bean

interface ImageLoader {

    var url: String

    var sizeParam: String

    fun loadImage()

}