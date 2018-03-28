package com.qyh.eyekotlin.mvp.videodetail

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.base.BaseActivity
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.utils.*
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_video_detail.*
import zlc.season.rxdownload3.RxDownload
import java.lang.ref.WeakReference

/**
 * @author 邱永恒
 *
 * @time 2018/2/26  8:45
 *
 * @desc 视频播放界面
 *
 */

class VideoDetailActivity : BaseActivity() {
    companion object {
        val MSG_IMAGE_LOADED = 101
        const val VIDEO_DATA = "video_data"
        // 本地缓存地址
        const val LOCAL_PATH = "local_path"
    }

    /**
     * 视频数据
     */
    private val videoBean by lazy { intent.extras.getParcelable<VideoBean>(VIDEO_DATA) }
    /**
     * 本地缓存地址(可能为空)
     */
    private val localPath by lazy { intent.extras.getString(LOCAL_PATH) }
    /**
     * 封面图片
     */
    private val imageView by lazy { ImageView(this) }
    /**
     * 屏幕旋转工具类
     */
    private val orientationUtils by lazy { OrientationUtils(this, gsy_player) }

    private val handler = Handler(Handler.Callback { msg ->
        when (msg?.what) {
            MSG_IMAGE_LOADED -> {
                // 设置封面图片
                gsy_player.thumbImageView = imageView
            }
        }
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        initView()
        initListener()
        prepareVideo()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        videoBean.blurred?.let { ImageLoadUtils.displayHigh(this, iv_bottom_bg, it) }
        tv_video_desc.text = videoBean.desc
        tv_video_desc.typeface = Typeface.createFromAsset(this.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_video_title.text = videoBean.title
        tv_video_title.typeface = Typeface.createFromAsset(this.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        tv_video_time.text = "${videoBean.category}/${TransformUtils.time(videoBean.duration?.toInt()!!)}"
        tv_video_favor.text = videoBean.collect?.toString()
        tv_video_share.text = videoBean.share?.toString()
        tv_video_reply.text = videoBean.reply?.toString()
    }


    private fun initListener() {
        tv_video_download.setOnClickListener {
            val playUrl = videoBean.playUrl?.let { SPUtils.getInstance(this, "downloads").getString(it) }
            if (TextUtils.isEmpty(playUrl)) {
                saveDownloadUrl(videoBean)
                addMission(videoBean.playUrl)
            } else {
                showToast("该视频已经缓存过了")
            }
        }
    }

    /**
     * 添加下载任务
     */
    private fun addMission(playUrl: String?) {
        RxDownload.start(playUrl!!)
                .subscribe(
                        { success ->
                            // 下载成功, 缓存视频链接, 用来判断是否下载过
                            SPUtils.getInstance(this, "downloads").put(playUrl, playUrl)
                            SPUtils.getInstance(this, "download_state").put(playUrl, true)
                        },
                        { error ->
                            showToast("下载失败, 请重试: $error")
                        })
    }

    /**
     * 初始化视频播放器
     */
    private fun prepareVideo() {
        if (TextUtils.isEmpty(localPath)) {
            // 在线播放
            gsy_player.setUp(videoBean.playUrl, false, null, null)
        } else {
            // 播放缓存
            Log.d(TAG, "离线缓存路径: $localPath")
            gsy_player.setUp(localPath, false, null, null)
        }

        // 封面图片
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageViewAsyncTask(handler, WeakReference(this), WeakReference(imageView)).execute(videoBean.feed)

        gsy_player.titleTextView.visibility = View.GONE
        gsy_player.backButton.visibility = View.VISIBLE
        gsy_player.setIsTouchWiget(true)

        //关闭自动旋转
        gsy_player.isRotateViewAuto = false
        gsy_player.isLockLand = false
        gsy_player.isShowFullAnimation = false
        gsy_player.isNeedLockFull = true
        gsy_player.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils.resolveByClick()
            // 第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            gsy_player.startWindowFullscreen(this, true, true)
        }

        gsy_player.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        orientationUtils.backToProtVideo()
        super.onBackPressed()
    }

}