package com.mvgreen.domain.bean

import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageLoader(private val url: String) {

    fun loadImage (view: ImageView) {
        Glide.with(view).load(url).into(view)
    }

}