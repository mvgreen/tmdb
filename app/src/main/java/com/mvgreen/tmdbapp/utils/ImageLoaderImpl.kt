package com.mvgreen.tmdbapp.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mvgreen.domain.bean.ImageLoader

class ImageLoaderImpl(
    private val view: ImageView,
    private val placeholder: Int,
    private val cropCircle: Boolean,
    val loadCallback: () -> Unit
) :
    ImageLoader {

    override var url: String = ""

    override var path: String? = null

    override var sizeParam: String? = null

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
        val fullPath = StringBuilder()
            .append(url)
            .append(path ?: "")
            .append(sizeParam?.let { it + view.width } ?: "")
            .toString()

        Glide
            .with(view)
            .load(fullPath)
            .placeholder(placeholder)
            .apply {
                if (cropCircle) {
                    apply(RequestOptions.circleCropTransform())
                }
            }
            .listener(onFailListener)
            .into(view)
    }

}