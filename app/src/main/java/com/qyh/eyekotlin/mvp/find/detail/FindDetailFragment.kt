package com.qyh.eyekotlin.mvp.find.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qyh.eyekotlin.MainActivity
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindDetailAdapter
import com.qyh.eyekotlin.base.BaseBackFragment
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.find.FindFragment
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_find_detail.*


/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/29  9:03
 *
 * @desc ${TODD}
 *
 */
class FindDetailFragment : BaseBackFragment(), FindDetailContract.View{
    private val list = ArrayList<Data>()
    private val adapter by lazy { FindDetailAdapter(R.layout.item_find_detail, list) }
    private val presenter by lazy { FindDetailPresenter(_mActivity, this, CompositeDisposable()) }
    private val title by lazy { arguments?.getString(FIND_DETAIL_TITLE) }
    private var start = 10
    private var strategy = "date"
    private var isRefresh = true
    private var isSlidingToLast = false
    companion object {
        const val FIND_DETAIL_TITLE = "find_detail_title"
        fun newInstance(bundle: Bundle): FindDetailFragment {
            val fragment = FindDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return attachToSwipeBack(inflater.inflate(R.layout.fragment_find_detail, container, false))
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initView()
        initListener()
        presenter.requestData(start, title!!, strategy)
    }

    private fun initView() {
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = title
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    }

    private fun initListener() {
        toolbar.setNavigationOnClickListener {
            _mActivity.onBackPressed()
        }
        refresh_layout.setOnRefreshListener {
            isRefresh = true
            start = 10
            presenter.requestData(10, title!!, strategy)
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.cover.feed, item.title, item.description, item.duration.toLong(), item.playUrl, item.category, item.cover.blurred, item.consumption.collectionCount, item.consumption.shareCount, item.consumption.replyCount, System.currentTimeMillis())
            context?.savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailFragment.VIDEO_DATA, videoBean)
            (parentFragment as FindFragment).start(VideoDetailFragment.newInstance(bundle))
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
                       presenter.requestData(start, title!!, strategy)
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
        context?.showToast(error)
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