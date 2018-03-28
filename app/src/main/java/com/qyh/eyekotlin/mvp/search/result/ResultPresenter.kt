package com.qyh.eyekotlin.mvp.search.result

import android.content.Context
import com.qyh.eyekotlin.model.ResultModel
import com.qyh.eyekotlin.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  10:30
 *
 * @desc ${TODD}
 *
 */
class ResultPresenter(val context: Context, val view: ResultContract.View) : ResultContract.Presenter {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val model by lazy { ResultModel() }
    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun start() {

    }

    override fun requestData(query: String, start: Int) {
        model.loadData(context, query, start)?.applySchedulers()?.subscribe(
                { bean ->
                    view.setData(bean)
                },
                { error ->
                    view.showError(error.toString())
                },
                {},
                { d -> compositeDisposable.add(d) })

    }
}