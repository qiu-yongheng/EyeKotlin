package com.qyh.eyekotlin.mvp.mine.watch

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindDetailAdapter
import com.qyh.eyekotlin.adapter.WatchAdapter
import com.qyh.eyekotlin.base.BaseActivity
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailActivity
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.savePlayUrl
import kotlinx.android.synthetic.main.activity_watch.*
import java.lang.ref.WeakReference

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  21:44
 *
 * @desc ${TODO}
 *
 */

class WatchActivity : BaseActivity() {
    private val datas = ArrayList<VideoBean>()
    private val adapter by lazy { WatchAdapter(R.layout.item_find_detail, datas) }
    private val handler = Handler(Handler.Callback {
        val list = it?.data?.getParcelableArrayList<VideoBean>("beans")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)

        initView()
        initListener()
        DataAsyncTask(WeakReference(handler), WeakReference(this)).execute()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "观看记录"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailActivity.VIDEO_DATA, item)
            newIntent<VideoDetailActivity>(bundle)
        }
    }
}