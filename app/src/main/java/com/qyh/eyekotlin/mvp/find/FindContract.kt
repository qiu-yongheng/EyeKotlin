package com.qyh.eyekotlin.mvp.find

import com.qyh.eyekotlin.base.BasePresenter
import com.qyh.eyekotlin.base.BaseView

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  22:30
 *
 * @desc ${TODO}
 *
 */

interface FindContract {
    interface View : BaseView<Presenter> {
        fun setData(beans : MutableList<FindBean>)
    }

    interface Presenter : BasePresenter {
        fun requestData()
    }
}