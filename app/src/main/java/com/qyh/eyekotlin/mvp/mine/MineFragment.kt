package com.qyh.eyekotlin.mvp.mine

import android.content.Intent
import android.graphics.Typeface
import android.view.View
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.base.BaseFragment
import com.qyh.eyekotlin.mvp.mine.advise.AdviseActivity
import com.qyh.eyekotlin.mvp.mine.cache.CacheActivity
import com.qyh.eyekotlin.mvp.mine.watch.WatchActivity
import com.qyh.eyekotlin.utils.newIntent
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  15:57
 *
 * @desc ${TODD}
 *
 */
class MineFragment : BaseFragment(), View.OnClickListener {
    override fun getLayoutResources(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        tv_advise.setOnClickListener(this)
        tv_watch.setOnClickListener(this)
        tv_save.setOnClickListener(this)
        tv_advise.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_watch.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_save.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_watch -> {
                // 观看记录
                activity?.newIntent<WatchActivity>()
            }
            R.id.tv_advise -> {
                // 了解作者
                activity?.newIntent<AdviseActivity>()
            }
            R.id.tv_save -> {
                // 缓存记录
                activity?.newIntent<CacheActivity>()
            }
        }
    }
}
