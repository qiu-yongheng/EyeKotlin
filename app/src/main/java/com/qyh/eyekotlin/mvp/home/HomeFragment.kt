package com.qyh.eyekotlin.mvp.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.HomeAdapter
import com.qyh.eyekotlin.model.bean.HomeBean
import com.qyh.eyekotlin.model.bean.HomeBean.IssueListBean.ItemListBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.ui.MainFragment
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_home.*
import me.yokeyword.fragmentation.SupportFragment
import java.util.regex.Pattern

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  9:42
 *
 * @desc ${TODD}
 *
 */
class HomeFragment : SupportFragment(), HomeContract.View {
    var isRefresh: Boolean = false
    lateinit var presenter: HomePresenter
    private val list by lazy { ArrayList<ItemListBean>() }
    private val adapter by lazy { HomeAdapter(R.layout.item_home, list) }
    lateinit var data: String
    private var isSlidingToLast = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
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
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { list.add(it) }
        adapter.notifyDataSetChanged()
    }

    fun initView() {
        presenter = HomePresenter(context!!, this)
        presenter.start()

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener {
            if (!isRefresh) {
                isRefresh = true
                presenter.start()
            }
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
                        presenter.moreData(data)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isSlidingToLast = dy > 0
            }
        })

        parentFragment
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.data?.cover?.feed, item.data?.title, item.data?.description, item.data?.duration, item.data?.playUrl, item.data?.category, item.data?.cover?.blurred, item.data?.consumption?.collectionCount, item.data?.consumption?.shareCount, item.data?.consumption?.replyCount, System.currentTimeMillis())
            context?.savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailFragment.VIDEO_DATA, videoBean)
            (parentFragment as MainFragment).start(MsgFragment.newInstance(mAdapter.getMsg(position)))
        }
    }

    override fun showError(error: String) {
        context?.showToast(error)
    }
}