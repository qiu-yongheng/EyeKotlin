package com.qyh.eyekotlin.mvp.home

import android.content.Context
import com.qyh.eyekotlin.model.HomeModel
import com.qyh.eyekotlin.utils.applySchedulers

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  11:22
 *
 * @desc ${TODD}
 *
 */
class HomePresenter(private var context: Context, var view: HomeContract.View) : HomeContract.Presenter {
    private val model: HomeModel by lazy {
        HomeModel()
    }
    override fun start() {
        requestData()
    }

    /**
     * 刷新数据
     */
    override fun requestData() {
        model.loadData(context, true, "0").applySchedulers().subscribe ({homeBean ->
            view.setData(homeBean)
        })
    }

    /**
     * 加载更多
     */
    fun moreData(data: String) {
        model.loadData(context, false, data).applySchedulers().subscribe({homeBean ->
            view.setData(homeBean)
        })
    }
}