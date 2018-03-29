package com.qyh.eyekotlin.mvp.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.HomeAdapter
import com.qyh.eyekotlin.base.BaseFragment
import com.qyh.eyekotlin.model.bean.HomeBean
import com.qyh.eyekotlin.model.bean.HomeBean.IssueListBean.ItemListBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailActivity
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.regex.Pattern

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  9:42
 *
 * @desc ${TODD}
 *
 */
class HomeFragment : BaseFragment(), HomeContract.View, SwipeRefreshLayout.OnRefreshListener {
    override fun showError(error: String) {
        context?.showToast(error)
    }

    var isRefresh: Boolean = false
    lateinit var presenter: HomePresenter
    private val list by lazy { ArrayList<ItemListBean>() }
    private val adapter by lazy { HomeAdapter(R.layout.item_home, list) }
    lateinit var data: String

    override fun getLayoutResources(): Int {
        return R.layout.fragment_home
    }

    /**
     * 获取到数据后, 加载数据到界面
     */
    override fun setData(bean: HomeBean) {
        // 正则表达式(获取分页URL)
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString() // 切割字符串

        // 隐藏刷新控件
        if (isRefresh) {
            isRefresh = false
            refreshLayout.isRefreshing = false
            if (list.size > 0) {
                list.clear()
            }
        }

        // !!: 如果为空, 抛出空指针异常
        bean.issueList!!
                .flatMap { it.itemList!!}
                .filter { it.type.equals("video") }
                .forEach { list.add(it) }
        adapter.notifyDataSetChanged()
    }

    override fun initView() {
        // 初始化presenter
        presenter = HomePresenter(context!!, this)
        presenter.start()

        // 初始化recyclerView
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        // 初始化SwipeRefreshLayout
        refreshLayout.setOnRefreshListener(this)
        // 监听recyclerView滚动, 触发加载更多
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == list.size - 1) {
                    presenter.moreData(data)
                }
            }
        })
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.data?.cover?.feed, item.data?.title, item.data?.description, item.data?.duration, item.data?.playUrl, item.data?.category, item.data?.cover?.blurred, item.data?.consumption?.collectionCount, item.data?.consumption?.shareCount, item.data?.consumption?.replyCount, System.currentTimeMillis())
            context?.savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailActivity.VIDEO_DATA, videoBean)
            activity?.newIntent<VideoDetailActivity>(bundle)
        }
    }

    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true
            presenter.start()
        }
    }
}