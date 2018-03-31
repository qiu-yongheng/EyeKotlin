package com.qyh.eyekotlin.mvp.hot

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.HotPagerAdapter
import com.qyh.eyekotlin.mvp.hot.rank.RankFragment
import kotlinx.android.synthetic.main.fragment_hot.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author 邱永恒
 *
 * @playDuration 2018/2/25  15:57
 *
 * @desc 热点
 *
 */
class HotFragment : SupportFragment() {
    private val tabList by lazy { arrayOf("周排行", "月排行", "总排行") }
    private val strategyList by lazy { arrayOf("weekly", "monthly", "historical") }
    private val fragments by lazy { ArrayList<Fragment>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        initFragments()
        view_pager.adapter = HotPagerAdapter(childFragmentManager, fragments, tabList)
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