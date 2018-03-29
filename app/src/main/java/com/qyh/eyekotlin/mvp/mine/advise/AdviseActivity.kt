package com.qyh.eyekotlin.mvp.mine.advise

import android.os.Bundle
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_advise.*

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  21:43
 *
 * @desc ${TODO}
 *
 */

class AdviseActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advise)
        settoolbar()
    }

    fun settoolbar(){
        setSupportActionBar(toolbar)
        val bar = supportActionBar
        bar?.title = "意见反馈"
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}