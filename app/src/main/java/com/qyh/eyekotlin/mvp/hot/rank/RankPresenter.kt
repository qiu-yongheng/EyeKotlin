package com.qyh.eyekotlin.mvp.hot.rank

import android.content.Context
import com.qyh.eyekotlin.model.RankModel
import io.reactivex.disposables.CompositeDisposable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  17:27
 *
 * @desc ${TODD}
 *
 */
class RankPresenter(context: Context, view: RankContract.View, compositeDisposable: CompositeDisposable) : RankContract.Presenter{
    private val model by lazy { RankModel() }
    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unSubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestData(strategy: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}