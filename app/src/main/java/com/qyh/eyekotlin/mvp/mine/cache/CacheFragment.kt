package com.qyh.eyekotlin.mvp.mine.cache

import android.content.ClipData.newIntent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.MainActivity
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.WatchAdapter
import com.qyh.eyekotlin.base.BaseBackFragment
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.mine.watch.DataAsyncTask
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.ui.MainFragment
import kotlinx.android.synthetic.main.fragment_watch.*
import org.w3c.dom.Attr
import java.lang.ref.WeakReference

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/29  21:43
 *
 * @desc ${TODO}
 *
 */

class CacheFragment : BaseBackFragment(){
    private val datas = ArrayList<VideoBean>()
    private val adapter by lazy { WatchAdapter(R.layout.item_find_detail, datas) }
    private val handler = Handler(Handler.Callback {
        val list = it?.data?.getParcelableArrayList<VideoBean>("downloads")
        if (list?.size == 0) {
            tv_hint.visibility = View.VISIBLE
        } else {
            tv_hint.visibility = View.GONE
            if (datas.size > 0) {
                datas.clear()
            }
            list?.let { datas.addAll(it) }
            adapter.notifyDataSetChanged()
        }
        true
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return attachToSwipeBack(inflater.inflate(R.layout.fragment_watch, container, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        DataAsyncTask(WeakReference(handler), WeakReference(context!!)).execute()
    }

    private fun initView() {
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = "缓存记录"
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        toolbar.setNavigationOnClickListener { _mActivity.onBackPressed() }
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailFragment.VIDEO_DATA, item)
            (parentFragment?.parentFragment as MainFragment).start(VideoDetailFragment.newInstance(bundle))
        }
    }
}