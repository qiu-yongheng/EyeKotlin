package com.qyh.eyekotlin.mvp.search.result

import android.content.ClipData.newIntent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qyh.eyekotlin.MainActivity
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FeedAdapter
import com.qyh.eyekotlin.base.BaseBackFragment
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment.Companion.VIDEO_DATA
import com.qyh.eyekotlin.ui.MainFragment
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_result.*


/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/28  9:59
 *
 * @desc ${TODD}
 *
 */

class ResultFragment : BaseBackFragment(), ResultContract.View {
    /**
     * 搜索关键字
     */
    private val keyWord: String by lazy { arguments!!.getString(RESULT_QUERY) }
    private val presenter: ResultContract.Presenter by lazy { ResultPresenter(context!!, this) }
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
        fun newInstance(bundle: Bundle) : ResultFragment{
            val fragment = ResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return attachToSwipeBack(inflater.inflate(R.layout.fragment_result, container, false))
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initView()
        initListener()
        presenter.requestData(keyWord, start)
    }

    private fun initView() {
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = "'$keyWord' 相关"
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    }

    private fun initListener() {
        /**
         * 返回键
         */
        toolbar.setNavigationOnClickListener { _mActivity.onBackPressed() }
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
            context?.savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VIDEO_DATA, videoBean)
            (parentFragment as MainFragment).start(VideoDetailFragment.newInstance(bundle))
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
        context?.showToast(error)
    }
}