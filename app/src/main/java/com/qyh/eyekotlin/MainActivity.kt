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

        if (findFragment(MainFragment::class.java) == null) {
            // 加载根Fragment
            loadRootFragment(R.id.fl_container, MainFragment.newInstance())
        }
    }
}