package com.qyh.eyekotlin.mvp.hot.rank

import com.qyh.eyekotlin.base.BasePresenter
import com.qyh.eyekotlin.base.BaseView
import com.qyh.eyekotlin.model.bean.HotBean

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  17:27
 *
 * @desc ${TODD}
 *
 */
interface RankContract {
    interface View : BaseView<Presenter> {
        fun setData(bean : HotBean)
    }
    interface Presenter : BasePresenter {
        fun requestData(strategy: String)
    }
}