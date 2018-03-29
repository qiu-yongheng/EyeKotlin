package com.qyh.eyekotlin.mvp.find

import android.content.Context
import com.qyh.eyekotlin.model.FindModel
import com.qyh.eyekotlin.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  8:29
 *
 * @desc ${TODD}
 *
 */
class FindPresenter(val context: Context?, val view: FindContract.View) : FindContract.Presenter {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val model by lazy { FindModel() }
    override fun start() {
        requestData()
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun requestData() {
        model.loadData()?.applySchedulers()?.subscribe({ list ->
            view.setData(list)
        }, { error ->
            view.showError(error.toString())
        }, {

        }, { d ->
            compositeDisposable.add(d)
        })
    }
}