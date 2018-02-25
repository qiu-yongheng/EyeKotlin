package com.qyh.eyekotlin.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author 邱永恒
 *
 * @time 2018/2/16  16:49
 *
 * @desc Fragment基类, 主要添加Fragment是否显示的判断
 *
 */

abstract class BaseFragment : Fragment() {
    // 根布局(可为空)
    var rootView: View? = null
    // 是否第一次加载
    var isFirst: Boolean = false
    // fragment是否正在显示
    var isFragmentVisiable: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResources(), container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * fragment显示隐藏的回调
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) isFragmentVisiable = true
        rootView ?: return
        if (!isFirst && isFragmentVisiable) {
            // 可见, 并且没有加载过
            onFragmentVisibleChange(true)
        } else if (isFragmentVisiable) {
            // 由可见 -> 不可见, 已经加载过
            onFragmentVisibleChange(false)
            isFragmentVisiable = false
        }
    }

    open protected fun onFragmentVisibleChange(b: Boolean) {

    }

    // 获取布局ID
    abstract fun getLayoutResources(): Int
    // 初始化
    abstract fun initView()
}