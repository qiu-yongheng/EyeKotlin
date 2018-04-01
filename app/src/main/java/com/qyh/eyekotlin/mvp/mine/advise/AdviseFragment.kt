package com.qyh.eyekotlin.mvp.mine.advise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.MainActivity
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.base.BaseBackFragment
import kotlinx.android.synthetic.main.fragment_advise.*

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/29  21:43
 *
 * @desc ${TODO}
 *
 */

class AdviseFragment : BaseBackFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return attachToSwipeBack(inflater.inflate(R.layout.fragment_advise, container, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initFragment()
    }

    private fun setToolbar(){
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(toolbar)
        val bar = mainActivity.supportActionBar
        bar?.title = "软件详情"
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            _mActivity.onBackPressed()
        }
    }

    private fun initFragment() {
        childFragmentManager.beginTransaction().replace(R.id.info_content, InfoPreferenceFragment()).commit()
    }
}