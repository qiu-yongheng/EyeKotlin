package com.qyh.eyekotlin.mvp.videodetail

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Handler
import android.widget.ImageView
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment.Companion.MSG_IMAGE_LOADED
import com.qyh.eyekotlin.utils.ImageLoadUtils
import java.lang.ref.WeakReference

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  17:13
 *
 * @desc 下载封面图片
 *
 */
class ImageViewAsyncTask(val handler: Handler, val fragment: WeakReference<VideoDetailFragment>, val imageview: WeakReference<ImageView>) : AsyncTask<String, Void, Drawable>() {

    /**
     * 在后台下载图片, 并返回drawable对象
     */
    override fun doInBackground(vararg params: String?): Drawable {
        val futureTarget = ImageLoadUtils.downloadOnly(fragment.get()!!, params[0]!!)

        return futureTarget.get()
    }

    /**
     * 执行后台任务后:
     * 1.
     * 2. 通知播放器设置封面
     */
    override fun onPostExecute(result: Drawable?) {
        super.onPostExecute(result)
        imageview.get()?.setImageDrawable(result)
        val message = handler.obtainMessage()
        message.what = MSG_IMAGE_LOADED
        handler.sendMessage(message)
    }
}