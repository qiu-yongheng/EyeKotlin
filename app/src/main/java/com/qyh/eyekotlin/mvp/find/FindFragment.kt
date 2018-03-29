package com.qyh.eyekotlin.mvp.find

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindAdapter
import com.qyh.eyekotlin.base.BaseFragment
import com.qyh.eyekotlin.model.bean.FindBean
import com.qyh.eyekotlin.mvp.find.detail.FindDetailActivity
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.showToast
import com.qyh.eyekotlin.view.DividerGridItemDecoration
import kotlinx.android.synthetic.main.fragment_find.*


/**
 * @author 邱永恒
 *
 * @time 2018/2/25  15:56
 *
 * @desc ${TODD}
 *
 */
class FindFragment : BaseFragment(), FindContract.View {
    private var list = ArrayList<FindBean>()
    private val adapter by lazy { FindAdapter(R.layout.item_find, list) }
    private val presenter by lazy { FindPresenter(context, this) }
    override fun getLayoutResources(): Int {
        return R.layout.fragment_find
    }

    override fun initView() {
        presenter.start()
        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(DividerGridItemDecoration(context!!, ContextCompat.getDrawable(context!!, R.drawable.grid_divi)!!))
        recycler_view.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val findBean = this.adapter.data[position]
            val bundle = Bundle()
            bundle.putString(FindDetailActivity.FIND_DETAIL_TITLE, findBean.name)
            activity?.newIntent<FindDetailActivity>(bundle)
        }
    }

    override fun showError(error: String) {
        context?.showToast(error)
    }

    override fun setData(beans: ArrayList<FindBean>) {
        list.clear()
        list.addAll(beans)
        adapter.notifyDataSetChanged()
    }
}