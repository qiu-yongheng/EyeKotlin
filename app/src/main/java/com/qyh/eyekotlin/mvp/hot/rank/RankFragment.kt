package com.qyh.eyekotlin.mvp.hot.rank

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindDetailAdapter
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_rank.*

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  14:56
 *
 * @desc ${TODD}
 *
 */
class RankFragment : Fragment(), RankContract.View {
    companion object {
        const val RANK_STRATEGY = "rank_strategy"
    }
    private val strategy by lazy { arguments?.getString(RANK_STRATEGY) }
    private val list = ArrayList<Data>()
    private val adapter by lazy { FindDetailAdapter(R.layout.item_find_detail, list) }
    private val presenter by lazy { RankPresenter(context!!, this, CompositeDisposable()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
    }

    private fun initListener() {

    }

    override fun showError(error: String) {

    }

    override fun setData(bean: HotBean) {

    }
}