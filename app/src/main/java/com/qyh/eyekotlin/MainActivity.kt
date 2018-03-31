package com.qyh.eyekotlin

import android.os.Bundle
import android.view.View
import com.qyh.eyekotlin.ui.MainFragment
import me.yokeyword.fragmentation.SupportActivity

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/30  11:05
 *
 * @desc ${TODD}
 *
 */
class MainActivity : SupportActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 沉浸式状态栏
//        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        // 隐藏导航栏
//        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance())
        }
    }

}