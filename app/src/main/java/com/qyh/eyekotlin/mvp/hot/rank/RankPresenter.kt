package com.qyh.eyekotlin.mvp.hot.rank

import android.content.Context
import com.qyh.eyekotlin.model.RankModel
import com.qyh.eyekotlin.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  17:27
 *
 * @desc ${TODD}
 *
 */
class RankPresenter(val context: Context, val view: RankContract.View, val compositeDisposable: CompositeDisposable) : RankContract.Presenter {
    private val model by lazy { RankModel() }
    override fun start() {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun requestData(strategy: String) {
        model.loadData(strategy)?.applySchedulers()?.subscribe({ bean ->
            view.setData(bean)
        }, { error ->
            view.showError(error.toString())
        }, {

        }, {
            compositeDisposable.add(it)
        })
    }
}