package com.qyh.eyekotlin.base

import android.os.Bundle
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import me.yokeyword.fragmentation.SupportActivity

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  10:05
 *
 * @desc ${TODD}
 *
 */
open class BaseActivity : SupportActivity(){
    open val TAG = this.javaClass.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        // 隐藏导航栏
        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}