package com.qyh.eyekotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  17:04
 *
 * @desc ${TODD}
 *
 */
class HotPagerAdapter(fm: FragmentManager, val fragments: ArrayList<Fragment>, val titles: Array<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}