package com.mvgreen.domain.bean

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageLoader(private val url: String) {

    fun loadImage (view: ImageView) {
        Glide
            .with(view)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(view)
    }

}