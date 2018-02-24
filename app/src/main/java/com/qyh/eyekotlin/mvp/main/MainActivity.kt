package com.qyh.eyekotlin.mvp.main

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.gyf.barlibrary.ImmersionBar
import com.qyh.eyekotlin.R
import kotlinx.android.synthetic.main.activity_main.*

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
    var mExitTime: Long = 0
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
    }

    private fun initToolbar() {
        var today = getToday()
    }

    private fun getToday(): String {

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

}