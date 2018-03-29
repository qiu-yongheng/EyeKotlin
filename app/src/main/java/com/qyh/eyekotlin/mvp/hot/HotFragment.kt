package com.qyh.eyekotlin.mvp.hot

import android.os.Bundle
import android.support.v4.app.Fragment
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.HotPagerAdapter
import com.qyh.eyekotlin.base.BaseFragment
import com.qyh.eyekotlin.mvp.hot.rank.RankFragment
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  15:57
 *
 * @desc ${TODD}
 *
 */
class HotFragment : BaseFragment() {


    private val tabList by lazy { arrayOf("周排行", "月排行", "总排行") }
    private val strategyList by lazy { arrayOf("weekly", "monthly", "historical") }
    private val fragments by lazy { ArrayList<Fragment>() }
    override fun getLayoutResources(): Int {
        return R.layout.fragment_hot
    }

    override fun initView() {
        initFragments()
        view_pager.adapter = HotPagerAdapter(fragmentManager!!, fragments, tabList)
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun initFragments() {
        for (s in strategyList) {
            val fragment = RankFragment()
            val bundle = Bundle()
            bundle.putString(RankFragment.RANK_STRATEGY, s)
            fragment.arguments = bundle
            fragments.add(fragment)
        }
    }
}