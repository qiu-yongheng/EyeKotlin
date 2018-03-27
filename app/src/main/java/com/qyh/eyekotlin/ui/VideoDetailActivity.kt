package com.qyh.eyekotlin.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.utils.ImageLoadUtils
import com.qyh.eyekotlin.utils.ObjectSaveUtils
import com.qyh.eyekotlin.utils.SPUtils
import com.qyh.eyekotlin.utils.showToast
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_video_detail.*
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.*
import zlc.season.rxdownload3.extension.ApkInstallExtension
import java.io.FileInputStream
import java.lang.ref.WeakReference

/**
 * @author 邱永恒
 *
 * @time 2018/2/26  8:45
 *
 * @desc ${TODD}
 *
 */
class VideoDetailActivity : AppCompatActivity() {
    companion object {
        val MSG_IMAGE_LOADED = 101
        val TAG = VideoDetailActivity::class.java.simpleName!!
    }

    var context: Context = this
    lateinit var imageView: ImageView
    lateinit var bean: VideoBean
    var isPlay: Boolean = false
    var isPause: Boolean = false
    lateinit var orientationUtils: OrientationUtils
    var handler = MyHandler(gsy_player, imageView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        bean = intent.getParcelableExtra<VideoBean>("data") // 获取传递过来的数据
        initView()
        prepareVideo()
    }

    private fun initView() {
        bean.blurred?.let { ImageLoadUtils.display(this, iv_bottom_bg, it) } // 显示背景图
        tv_video_desc.text = bean.desc
        tv_video_desc.typeface = Typeface.createFromAsset(this.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_video_title.text = bean.title
        tv_video_title.typeface = Typeface.createFromAsset(this.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        val category = bean.category
        val duration = bean.duration
        val minute = duration?.div(60)
        val second = duration?.minus((minute?.times(60)) as Long)
        val realMinute: String
        val realSecond: String
        realMinute = if (minute!! < 10) {
            "0" + minute
        } else {
            minute.toString()
        }
        realSecond = if (second!! < 10) {
            "0" + second
        } else {
            second.toString()
        }
        tv_video_time.text = "$category / $realMinute'$realSecond''"
        tv_video_favor.text = bean.collect.toString()
        tv_video_share.text = bean.share.toString()
        tv_video_reply.text = bean.share.toString()
        tv_video_download.setOnClickListener {
            //点击下载
            val url = bean.playUrl?.let { it1 -> SPUtils.getInstance(this, "downloads").getString(it1) }
            if (url.equals("")) {
                var count = SPUtils.getInstance(this, "downloads").getInt("count")
                count = if (count != -1) {
                    count.inc() // count++
                } else {
                    1
                }
                SPUtils.getInstance(this, "downloads").put("count", count)
                ObjectSaveUtils.saveObject(this, "download$count", bean)
                addMission(bean.playUrl, count)
            } else {
                showToast("该视频已经缓存过了")
            }
        }
    }

    /**
     * 添加下载任务
     */
    private fun addMission(playUrl: String?, count: Int) {
        RxDownload.create(Mission(playUrl!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->
                    //开始下载
                    setActionText(status)
                    SPUtils.getInstance(this, "downloads").put(playUrl, playUrl)
                    SPUtils.getInstance(this, "download_state").put(playUrl, true)
                }
    }

    private fun setActionText(status: Status?) {
        val text = when (status) {
            is Normal -> "开始"
            is Suspend -> "已暂停"
            is Waiting -> "等待中"
            is Downloading -> "暂停"
            is Failed -> "失败"
            is Succeed -> "安装"
            is ApkInstallExtension.Installing -> "安装中"
            is ApkInstallExtension.Installed -> "打开"
            else -> ""
        }
        showToast(text)
    }

    /**
     * 准备播放
     */
    private fun prepareVideo() {
        // 设置播放路径
        val uri = intent.getStringExtra("localFile")
        if (TextUtils.isEmpty(uri)) {
            gsy_player.setUp(uri, false, null, null)
        } else {
            gsy_player.setUp(bean.playUrl, false, null, null)
        }

        // 设置封面
        imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageViewAsyncTask(handler, this, imageView).execute(bean.feed) // 下载封面, 并设置

        gsy_player.titleTextView.visibility = View.GONE
        gsy_player.backButton.visibility = View.VISIBLE
        orientationUtils = OrientationUtils(this, gsy_player)
    }

    class ImageViewAsyncTask(val handler: Handler, activity: VideoDetailActivity, imageView: ImageView) : AsyncTask<String, Void, String>() {
        private val activity = WeakReference<VideoDetailActivity>(activity)
        private val imageView = WeakReference<ImageView>(imageView)
        private var inputString: FileInputStream? = null

        /**
         * 下载图片, 返回缓存地址
         */
        override fun doInBackground(vararg params: String?): String {
            val future = ImageLoadUtils.downloadOnly(activity.get()!!, params[0]!!, 100, 100)
            val cacheFile = future?.get()
            return cacheFile?.absolutePath!!
        }

        /**
         * 加载图片
         */
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            inputString = FileInputStream(result)
            val bitmap = BitmapFactory.decodeStream(inputString)
            imageView.get()?.setImageBitmap(bitmap)
            val message = handler.obtainMessage()
            message.what = MSG_IMAGE_LOADED
            handler.sendMessage(message)
        }
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        orientationUtils.releaseListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!gsy_player.isIfCurrentIsFullscreen) {
                    gsy_player.startWindowFullscreen(context, true, true)
                }
            } else {
                if (gsy_player.isIfCurrentIsFullscreen) {
                    gsy_player.onBackFullscreen()
                }
                orientationUtils.isEnable = true
            }
        }
    }

    class MyHandler(gsy_player: StandardGSYVideoPlayer, imageView: ImageView) : Handler() {
        private val gsyPlayer = WeakReference<StandardGSYVideoPlayer>(gsy_player)
        private val iv = WeakReference<ImageView>(imageView)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_IMAGE_LOADED -> {
                    gsyPlayer.get()?.thumbImageView = iv.get()!!
                }
            }
        }
    }
}