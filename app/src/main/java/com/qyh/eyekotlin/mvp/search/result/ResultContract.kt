package com.qyh.eyekotlin.mvp.search.result

import com.qyh.eyekotlin.base.BasePresenter
import com.qyh.eyekotlin.base.BaseView
import com.qyh.eyekotlin.model.bean.HotBean

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  9:24
 *
 * @desc ${TODD}
 *
 */
interface ResultContract {
    interface View : BaseView<Presenter> {
        fun setData(bean: HotBean)
    }

    interface Presenter : BasePresenter {
        fun requestData(query: String, start: Int)
    }
}