package com.qyh.eyekotlin.mvp.find.detail

import com.qyh.eyekotlin.base.BasePresenter
import com.qyh.eyekotlin.base.BaseView
import com.qyh.eyekotlin.model.bean.HotBean

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  10:03
 *
 * @desc ${TODD}
 *
 */
interface FindDetailContract {
    interface View : BaseView<Presenter> {
        fun setData(bean: HotBean)
    }

    interface Presenter : BasePresenter {
        fun requestData(start: Int, categoryName: String, strategy: String)
    }
}