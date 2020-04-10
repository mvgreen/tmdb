package com.mvgreen.tmdbapp.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mvgreen.data.exception.NetworkException
import com.mvgreen.domain.bean.ImageLoader
import com.mvgreen.tmdbapp.R

class ImageLoaderImpl(private val view: ImageView, val loadCallback: () -> Unit) : ImageLoader {

    override lateinit var url: String
    override lateinit var sizeParam: String

    private val onFailListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            loadCallback.invoke()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

    }

    override fun loadImage() {
        Glide
            .with(view)
            .load(url + sizeParam + view.width)
            .placeholder(R.drawable.ic_profile_stub)
            .apply(RequestOptions.circleCropTransform())
            .listener(onFailListener)
            .into(view)
    }

}