package com.qyh.eyekotlin.mvp.search.result

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FeedAdapter
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment.Companion.VIDEO_DATA
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_result.*


/**
 * @author 邱永恒
 *
 * @time 2018/3/28  9:59
 *
 * @desc ${TODD}
 *
 */

class ResultActivity : AppCompatActivity(), ResultContract.View {
    /**
     * 搜索关键字
     */
    private val keyWord: String by lazy { intent.extras.getString(RESULT_QUERY) }
    private val presenter: ResultContract.Presenter by lazy { ResultPresenter(this, this) }
    /**
     * 请求数据个数
     */
    private var start = 10
    private val adapter by lazy { FeedAdapter(R.layout.item_feed, list) }
    /**
     * 列表数据源
     */
    private var list = ArrayList<Data>()
    /**
     * 是否在刷新
     */
    private var isRefresh = true
    /**
     * 是否向下滑动
     */
    private var isSlidingToLast = false

    companion object {
        const val RESULT_QUERY = "keyWord"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initView()
        initListener()
        presenter.requestData(keyWord, start)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "'$keyWord' 相关"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        /**
         * 返回键
         */
        toolbar.setNavigationOnClickListener { onBackPressed() }
        /**
         * 刷新
         */
        refresh_layout.setOnRefreshListener {
            if (!isRefresh) {
                isRefresh = true
                start = 10
                presenter.requestData(keyWord, start)
            }
        }
        /**
         * 下拉加载更多
         */
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取布局管理器
                    val manager = recyclerView?.layoutManager as LinearLayoutManager
                    // 获取最后一个完全显示的item position
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    // 获取当前显示的item的数量
                    val totalItemCount = manager.itemCount

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == totalItemCount - 1 && isSlidingToLast) {
                        //加载更多
                        start += 10
                        presenter.requestData(keyWord, start)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isSlidingToLast = dy > 0
            }
        })

        /**
         * 打开视频详情
         */
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.cover.feed, item.title, item.description, item.duration.toLong(), item.playUrl, item.category, item.cover.blurred, item.consumption.collectionCount, item.consumption.shareCount, item.consumption.replyCount, System.currentTimeMillis())
            savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VIDEO_DATA, videoBean)
            newIntent<VideoDetailFragment>(bundle)
        }
    }

    /**
     * 加载数据到数据源
     * 1. 如果是刷新, 清空数据源
     * 2. 加载数据到数据源
     */
    override fun setData(bean: HotBean) {
        if (isRefresh) {
            isRefresh = false
            refresh_layout.isRefreshing = false
            if (list.size > 0) {
                list.clear()
            }
        }

        bean.itemList.forEach { list.add(it.data) }
        adapter.notifyDataSetChanged()
    }

    override fun showError(error: String) {
        showToast(error)
    }
}