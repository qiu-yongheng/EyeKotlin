package com.qyh.eyekotlin.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.mvp.find.FindFragment
import com.qyh.eyekotlin.mvp.home.HomeFragment
import com.qyh.eyekotlin.mvp.hot.HotFragment
import com.qyh.eyekotlin.mvp.mine.MineFragment
import com.qyh.eyekotlin.mvp.search.SearchFragment
import com.qyh.eyekotlin.mvp.search.SearchFragment.Companion.SEARCH_TAG
import com.qyh.eyekotlin.utils.CalendarUtils
import com.qyh.eyekotlin.utils.showToast
import com.qyh.eyekotlin.view.BottomBar
import com.qyh.eyekotlin.view.BottomBarTab
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/30  13:42
 *
 * @desc ${TODD}
 *
 */
class MainFragment : SupportFragment() {
    private var homeFragment: HomeFragment? = null
    private var findFragment: FindFragment? = null
    private var hotFragment: HotFragment? = null
    private var mineFragment: MineFragment? = null
    private val searchFragment by lazy { SearchFragment() }
    private var exitTime: Long = 0
    private val fragments = arrayOfNulls<SupportFragment>(4)
    private val WAIT_TIME = 2000L
    private var TOUCH_TIME: Long = 0

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initView()
        initListener()
        initFragments()
    }

    private fun initView() {
        tv_bar_title.text = CalendarUtils.getToday()
        tv_bar_title.typeface = Typeface.createFromAsset(activity?.assets, "fonts/Lobster-1.4.otf")
        bottom_bar
                .addItem(BottomBarTab(context, R.mipmap.home_normal, "首页"))
                .addItem(BottomBarTab(context, R.mipmap.find_normal, "发现"))
                .addItem(BottomBarTab(context, R.mipmap.hot_normal, "热门"))
                .addItem(BottomBarTab(context, R.mipmap.mine_normal, "我的"))
    }

    private fun initListener() {
        iv_search.setOnClickListener {
            // SearchFragment继承DialogFragment, 像使用dialog一样使用fragment
            searchFragment.setTargetFragment(this, 1)
            searchFragment.show(fragmentManager, SEARCH_TAG)
        }
        bottom_bar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(fragments[position], fragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {

            }
        })
    }

    private fun initFragments() {
        homeFragment = findChildFragment(HomeFragment::class.java)
        if (homeFragment == null) {
            homeFragment = HomeFragment()
            findFragment = FindFragment()
            hotFragment = HotFragment()
            mineFragment = MineFragment()
            loadMultipleRootFragment(R.id.fl_content, 0, homeFragment, findFragment, hotFragment, mineFragment)
        } else {
            findFragment = findChildFragment(FindFragment::class.java)
            hotFragment = findChildFragment(HotFragment::class.java)
            mineFragment = findChildFragment(MineFragment::class.java)
        }

        fragments[0] = homeFragment
        fragments[1] = findFragment
        fragments[2] = hotFragment
        fragments[3] = mineFragment
    }

    override fun onBackPressedSupport(): Boolean {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            activity?.finish()
        } else {
            TOUCH_TIME = System.currentTimeMillis()
            context?.showToast("再按一次返回键退出")
        }
        return true
    }
}