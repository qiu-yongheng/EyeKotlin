package com.qyh.eyekotlin.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import com.qyh.eyekotlin.MainActivity
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.utils.newIntent
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author 邱永恒
 *
 * @time 2018/2/16  15:38
 *
 * @desc 欢迎页
 *
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view = View.inflate(this, R.layout.activity_splash, null)
        setContentView(view)
        initView()
        setAnimator(view)
    }

    /**
     * 设置属性动画
     */
    private fun setAnimator(view: View) {
        // 布局放大动画
        view.animate().scaleX(1.02f).scaleY(1.02f).duration = 2000

        // logo渐变动画
        val alphaAnimation = AlphaAnimation(0.1f, 1.0f)
        alphaAnimation.duration = 2000
        // logo缩放动画
        val scaleAnimation = ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 2000
        // 动画集
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        iv_icon_splash.startAnimation(animationSet)
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                // 扩展函数, 启动界面
                newIntent<MainActivity>()
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

    /**
     * 加载字体
     */
    private fun initView() {
        val font: Typeface = Typeface.createFromAsset(this.assets, "fonts/Lobster-1.4.otf")
        tv_name_english.typeface = font
        tv_english_intro.typeface = font
    }
}
