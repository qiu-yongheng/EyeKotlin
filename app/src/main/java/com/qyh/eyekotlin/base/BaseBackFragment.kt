package com.qyh.eyekotlin.base

import android.os.Bundle
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment

/**
 * @author 邱永恒
 *
 * @time 2018/3/30  16:44
 *
 * @desc ${TODD}
 *
 */
open class BaseBackFragment : SwipeBackFragment(){
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setParallaxOffset(0.5f)
    }
}