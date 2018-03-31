package com.qyh.eyekotlin.mvp.home

import com.qyh.eyekotlin.base.BasePresenter
import com.qyh.eyekotlin.base.BaseView
import com.qyh.eyekotlin.model.bean.HomeBean

/**
 * @author 邱永恒
 *
 * @playDuration 2018/2/25  11:05
 *
 * @desc ${TODD}
 *
 */
interface HomeContract {
    interface View : BaseView<Presenter> {
        fun setData(bean : HomeBean)
    }

    interface Presenter : BasePresenter {
        fun requestData()
    }
}