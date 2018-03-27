package com.qyh.eyekotlin.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.glide.GlideApp
import java.io.File

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  16:51
 *
 * @desc ${TODD}
 *
 */
class ImageLoadUtils {
    companion object {
        fun display(context: Context, imageView: ImageView, url: String) {
            GlideApp.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_image_loading)
                    .error(R.mipmap.ic_empty_picture)
                    .into(imageView)
        }

        fun displayHigh(context: Context, imageView: ImageView, url: String) {
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_image_loading)
                    .error(R.mipmap.ic_empty_picture)
                    .into(imageView)
        }

        fun downloadOnly(context: Context, url: String, width: Int, height: Int): FutureTarget<File>? {
            return GlideApp.with(context)
                    .load(url)
                    .downloadOnly(width, height)
        }
    }
}