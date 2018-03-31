package com.qyh.eyekotlin.mvp.videodetail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.base.BaseBackFragment
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.utils.*
import com.qyh.eyekotlin.utils.helper.PermissionHelper
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_video_detail.*
import zlc.season.rxdownload3.RxDownload
import java.lang.ref.WeakReference



/**
 * @author 邱永恒
 *
 * @playDuration 2018/2/26  8:45
 *
 * @desc 视频播放界面
 *
 */

class VideoDetailFragment : BaseBackFragment() {
    companion object {
        val MSG_IMAGE_LOADED = 101
        const val VIDEO_DATA = "video_data"
        // 本地缓存地址
        const val LOCAL_PATH = "local_path"

        fun newInstance(bundle: Bundle): VideoDetailFragment {
            val fragment = VideoDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 视频数据
     */
    private val videoBean by lazy { arguments?.getParcelable<VideoBean>(VIDEO_DATA) }
    /**
     * 本地缓存地址(可能为空)
     */
    private val localPath by lazy { arguments?.getString(LOCAL_PATH) }
    /**
     * 封面图片
     */
    private val imageView by lazy { ImageView(context) }
    /**
     * 设置旋转
     */
    private lateinit var orientationUtils: OrientationUtils
    private var isPlay = false

    private val handler = Handler(Handler.Callback { msg ->
        when (msg?.what) {
            MSG_IMAGE_LOADED -> {
                // 设置封面图片
                gsy_player.thumbImageView = imageView
            }
        }
        true
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return attachToSwipeBack(inflater.inflate(R.layout.fragment_video_detail, container, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        prepareVideo()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        videoBean?.blurred?.let { ImageLoadUtils.displayHigh(context!!, iv_bottom_bg, it) }
        tv_video_desc.text = videoBean?.desc
        tv_video_desc.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_video_title.text = videoBean?.title
        tv_video_title.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        tv_video_time.text = "${videoBean?.category}/${TransformUtils.playDuration(videoBean?.duration?.toInt()!!)}"
        tv_video_favor.text = videoBean?.collect?.toString()
        tv_video_share.text = videoBean?.share?.toString()
        tv_video_reply.text = videoBean?.reply?.toString()
    }


    private fun initListener() {
        tv_video_download.setOnClickListener {
            val playUrl = videoBean?.playUrl?.let { SPUtils.getInstance(context!!, "downloads").getString(it) }
            if (TextUtils.isEmpty(playUrl)) {
                context?.saveDownloadUrl(videoBean!!)
                addMission(videoBean?.playUrl)
            } else {
                context?.showToast("该视频已经缓存过了")
            }
        }
    }

    /**
     * 添加下载任务
     */
    private fun addMission(playUrl: String?) {
        PermissionHelper.requestStorage(object : PermissionHelper.OnPermissionGrantedListener {
            override fun onPermissionGranted() {
                RxDownload.create(playUrl!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { status ->
                                    Log.d("下载进度", "下载进度: ${status.percent()}")
                                },
                                { error ->
                                    error.printStackTrace()
                                    Log.d("下载失败", "下载失败")
                                    context?.showToast("下载失败, 请重试")
                                },
                                {
                                    // 下载成功, 缓存视频链接, 用来判断是否下载过
                                    SPUtils.getInstance(context!!, "downloads").put(playUrl, playUrl)
                                    SPUtils.getInstance(context!!, "download_state").put(playUrl, true)
                                })
                RxDownload.start(playUrl).subscribe()
            }
        })
    }

    /**
     * 初始化视频播放器
     */
    private fun prepareVideo() {
        if (TextUtils.isEmpty(localPath)) {
            // 在线播放
            gsy_player.setUp(videoBean?.playUrl, false, null, videoBean?.title)
        } else {
            // 播放缓存
            //Log.d(TAG, "离线缓存路径: $localPath")
            gsy_player.setUp(localPath, false, null, videoBean?.title)
        }

        // 封面图片
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageViewAsyncTask(handler, WeakReference(context!!), WeakReference(imageView)).execute(videoBean?.feed)
        // 显示标题
        gsy_player.titleTextView.visibility = View.VISIBLE
        // 设置返回键
        gsy_player.backButton.visibility = View.VISIBLE
        // 设置旋转
        orientationUtils = OrientationUtils(activity, gsy_player)
        //初始化不打开外部的旋转
        orientationUtils.isEnable = false
        // 是否可以滑动调整
        gsy_player.setIsTouchWiget(true)
        //关闭自动旋转
        gsy_player.isRotateViewAuto = false
        gsy_player.isLockLand = false
        gsy_player.isShowFullAnimation = false
        gsy_player.isNeedLockFull = true
        // 设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        gsy_player.fullscreenButton.setOnClickListener {
            orientationUtils.resolveByClick()
            gsy_player.startWindowFullscreen(context, true, true)
        }
        gsy_player.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                //开始播放了才能旋转和全屏
                orientationUtils.isEnable = true
                isPlay = true
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                orientationUtils.backToProtVideo()
            }
        })
        gsy_player.setLockClickListener { view, lock ->
            // 配合下方的onConfigurationChanged
            orientationUtils.isEnable = !lock
        }
        // 设置返回键功能
        gsy_player.backButton.setOnClickListener { _mActivity.onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        gsy_player.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        gsy_player.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        orientationUtils.releaseListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay) {
            gsy_player.onConfigurationChanged(activity, newConfig, orientationUtils, true, true)
        }
    }

    override fun onBackPressedSupport(): Boolean {
        orientationUtils.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(context)) {
            // 消费事件, 不向上传递
            return true
        }
        return super.onBackPressedSupport()
    }

}