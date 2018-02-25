package com.qyh.eyekotlin.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.gyf.barlibrary.ImmersionBar
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.mvp.find.FindFragment
import com.qyh.eyekotlin.mvp.home.HomeFragment
import com.qyh.eyekotlin.mvp.hot.HotFragment
import com.qyh.eyekotlin.mvp.mine.MineFragment
import com.qyh.eyekotlin.mvp.search.SEARCH_TAG
import com.qyh.eyekotlin.mvp.search.SearchFragment
import com.qyh.eyekotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author 邱永恒
 *
 * @time 2018/2/16  15:38
 *
 * @desc ${TODO}
 *
 */

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var homeFragment: HomeFragment? = null
    var findFragment: FindFragment? = null
    var hotFragment: HotFragment? = null
    var mineFragment: MineFragment? = null
    var exitTime: Long = 0
    var toast: Toast? = null
    // 延迟初始化变量(因为kotlin不能使用null初始化)
    lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 沉浸式状态栏(调用java代码)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()

        // 获取当前屏幕的窗口, 当Activity不可见时, 为null
        val window = window
        // 获取当前屏幕的参数
        val params = window.attributes

        setRadioButton()
        initToolbar()
        initFragment(savedInstanceState)
    }

    private fun initToolbar() {
        // 获取当前日期
        var today = getToday()
        tv_bar_title.text = today
        tv_bar_title.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-1.4.otf")
        iv_search.setOnClickListener {
            if (rb_mine.isChecked) {
                // TODO: 点击设置
            } else {
                // 点击搜索
                searchFragment = SearchFragment()
                // SearchFragment继承DialogFragment
                searchFragment.show(supportFragmentManager, SEARCH_TAG)
            }
        }
    }

    private fun getToday(): String {
        val list: Array<String> = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        var index = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // 异常情况(获取栈里所有的fragment)
            for (item in supportFragmentManager.fragments) {
                when (item) {
                    is HomeFragment -> homeFragment = item
                    is FindFragment -> findFragment = item
                    is HotFragment -> hotFragment = item
                    is MineFragment -> mineFragment = item
                }
            }
        } else {
            // 创建fragment并添加到fragmentManager
            homeFragment = HomeFragment()
            findFragment = FindFragment()
            mineFragment = MineFragment()
            hotFragment = HotFragment()
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, homeFragment)
            fragmentTrans.add(R.id.fl_content, findFragment)
            fragmentTrans.add(R.id.fl_content, hotFragment)
            fragmentTrans.add(R.id.fl_content, mineFragment)
            fragmentTrans.commit()
        }
        supportFragmentManager.beginTransaction()
                .show(homeFragment)
                .hide(findFragment)
                .hide(hotFragment)
                .hide(mineFragment)
                .commit()
    }

    /**
     * 初始化底部导航栏的RadioButton
     */
    private fun setRadioButton() {
        rb_home.isChecked = true
        rb_home.setTextColor(resources.getColor(R.color.black))

        rb_home.setOnClickListener(this)
        rb_find.setOnClickListener(this)
        rb_hot.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clearState()
        when (v?.id) {
            R.id.rb_find -> {
                rb_find.isChecked = true
                rb_find.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(findFragment)
                        .hide(homeFragment)
                        .hide(mineFragment)
                        .hide(hotFragment)
                        .commit()
                tv_bar_title.text = "Discover"
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.mipmap.icon_search)
            }
            R.id.rb_home -> {
                rb_home.isChecked = true
                rb_home.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(homeFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .hide(hotFragment)
                        .commit()
                tv_bar_title.text = getToday()
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.mipmap.icon_search)
            }
            R.id.rb_hot -> {
                rb_hot.isChecked = true
                rb_hot.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(hotFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .hide(homeFragment)
                        .commit()
                tv_bar_title.text = "Ranking"
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.mipmap.icon_search)
            }
            R.id.rb_mine -> {
                rb_mine.isChecked = true
                rb_mine.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mineFragment)
                        .hide(findFragment)
                        .hide(homeFragment)
                        .hide(hotFragment)
                        .commit()
                tv_bar_title.visibility = View.INVISIBLE
                iv_search.setImageResource(R.mipmap.icon_setting)
            }
        }
    }

    /**
     * 清除状态
     */
    private fun clearState() {
        rg_root.clearCheck()
        rb_home.setTextColor(resources.getColor(R.color.gray))
        rb_find.setTextColor(resources.getColor(R.color.gray))
        rb_hot.setTextColor(resources.getColor(R.color.gray))
        rb_mine.setTextColor(resources.getColor(R.color.gray))
    }

    override fun onPause() {
        super.onPause()
        toast?.cancel()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (System.currentTimeMillis().minus(exitTime) <= 3000) {
                    finish()
                    toast?.cancel()
                } else {
                    exitTime = System.currentTimeMillis()
                    toast = showToast("再按一次退出应用")
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}