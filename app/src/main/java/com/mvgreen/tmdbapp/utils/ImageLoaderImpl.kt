package com.mvgreen.tmdbapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.tmdbapp.R

class ImageLoaderImpl(private val view: ImageView) : ImageLoader {

    override lateinit var url: String
    override lateinit var sizeParam: String

    override fun loadImage() {
        Glide
            .with(view)
            .load(url + sizeParam + view.width)
            .placeholder(R.drawable.ic_profile_stub)
            .apply(RequestOptions.circleCropTransform())
            .into(view)
    }

}