package com.qyh.eyekotlin.mvp.find.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindDetailAdapter
import com.qyh.eyekotlin.base.BaseActivity
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailActivity
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_find_detail.*


/**
 * @author 邱永恒
 *
 * @time 2018/3/29  9:03
 *
 * @desc ${TODD}
 *
 */
class FindDetailActivity : BaseActivity(), FindDetailContract.View{
    private val list = ArrayList<Data>()
    private val adapter by lazy { FindDetailAdapter(R.layout.item_find_detail, list) }
    private val presenter by lazy { FindDetailPresenter(this, this, CompositeDisposable()) }
    private val title by lazy { intent.extras.getString(FIND_DETAIL_TITLE) }
    private var start = 10
    private var strategy = "date"
    private var isRefresh = true
    private var isSlidingToLast = false
    companion object {
        const val FIND_DETAIL_TITLE = "find_detail_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_detail)
        initView()
        initListener()
        presenter.requestData(start, title, strategy)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        refresh_layout.setOnRefreshListener {
            isRefresh = true
            start = 10
            presenter.requestData(10, title, strategy)
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.cover.feed, item.title, item.description, item.duration.toLong(), item.playUrl, item.category, item.cover.blurred, item.consumption.collectionCount, item.consumption.shareCount, item.consumption.replyCount, System.currentTimeMillis())
            savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailActivity.VIDEO_DATA, videoBean)
            newIntent<VideoDetailActivity>(bundle)
        }
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
                       presenter.requestData(start, title, strategy)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isSlidingToLast = dy > 0
            }
        })

    }

    override fun showError(error: String) {
        showToast(error)
    }

    override fun setData(bean: HotBean) {
        if (isRefresh) {
            isRefresh = false
            refresh_layout.isRefreshing = false
            list.clear()
        }

        bean.itemList.forEach { list.add(it.data) }
        adapter.notifyDataSetChanged()
    }

}