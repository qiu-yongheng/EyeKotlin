package com.qyh.eyekotlin.mvp.find.detail

import android.content.Context
import com.qyh.eyekotlin.model.FindDetailModel
import com.qyh.eyekotlin.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  10:04
 *
 * @desc ${TODD}
 *
 */
class FindDetailPresenter(val context: Context, val view: FindDetailContract.View, val compositeDisposable: CompositeDisposable) : FindDetailContract.Presenter{
    private val model by lazy { FindDetailModel() }
    override fun start() {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun requestData(start: Int, categoryName: String, strategy: String) {
        model.loadData(start, categoryName, strategy)?.applySchedulers()?.subscribe({bean ->
            view.setData(bean)
        }, {error ->
            view.showError(error.toString())
        }, {

        }, {d ->
            compositeDisposable.add(d)
        })
    }
}