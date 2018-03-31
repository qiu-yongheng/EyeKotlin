package com.qyh.eyekotlin.mvp.find

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindAdapter
import com.qyh.eyekotlin.model.bean.FindBean
import com.qyh.eyekotlin.mvp.find.detail.FindDetailFragment
import com.qyh.eyekotlin.ui.MainFragment
import com.qyh.eyekotlin.utils.showToast
import com.qyh.eyekotlin.view.DividerGridItemDecoration
import kotlinx.android.synthetic.main.fragment_find.*
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author 邱永恒
 *
 * @playDuration 2018/2/25  15:56
 *
 * @desc 发现
 *
 */
class FindFragment : SupportFragment(), FindContract.View {
    private var list = ArrayList<FindBean>()
    private val adapter by lazy { FindAdapter(R.layout.item_find, list) }
    private val presenter by lazy { FindPresenter(context, this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    fun initView() {
        presenter.start()
        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(DividerGridItemDecoration(context!!, ContextCompat.getDrawable(context!!, R.drawable.grid_divi)!!))
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        adapter.setOnItemClickListener { adapter, view, position ->
            val findBean = this.adapter.data[position]
            val bundle = Bundle()
            bundle.putString(FindDetailFragment.FIND_DETAIL_TITLE, findBean.name)
            (parentFragment as MainFragment).start(FindDetailFragment.newInstance(bundle))
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