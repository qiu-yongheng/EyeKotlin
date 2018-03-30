package com.qyh.eyekotlin

import android.os.Bundle
import com.qyh.eyekotlin.base.BaseActivity
import com.qyh.eyekotlin.ui.SplashFragment

/**
 * @author 邱永恒
 *
 * @time 2018/3/30  11:05
 *
 * @desc ${TODD}
 *
 */
class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findFragment(SplashFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, SplashFragment.newInstance())
        }
    }
}