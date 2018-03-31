package com.qyh.eyekotlin.mvp.hot.rank

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.FindDetailAdapter
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.hot.HotFragment
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailFragment
import com.qyh.eyekotlin.ui.MainFragment
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.savePlayUrl
import com.qyh.eyekotlin.utils.showToast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_rank.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  14:56
 *
 * @desc ${TODD}
 *
 */
class RankFragment : SupportFragment(), RankContract.View {
    companion object {
        const val RANK_STRATEGY = "rank_strategy"
    }
    private val strategy by lazy { arguments?.getString(RANK_STRATEGY) }
    private val list = ArrayList<Data>()
    private val adapter by lazy { FindDetailAdapter(R.layout.item_find_detail, list) }
    private val presenter by lazy { RankPresenter(context!!, this, CompositeDisposable()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        presenter.requestData(strategy!!)
    }

    private fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    }

    private fun initListener() {
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = this.adapter.data[position]
            val videoBean = VideoBean(item.cover.feed, item.title, item.description, item.duration.toLong(), item.playUrl, item.category, item.cover.blurred, item.consumption.collectionCount, item.consumption.shareCount, item.consumption.replyCount, System.currentTimeMillis())
            context?.savePlayUrl(videoBean)
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailFragment.VIDEO_DATA, videoBean)
            (parentFragment?.parentFragment as MainFragment).start(VideoDetailFragment.newInstance(bundle))
        }
    }

    override fun showError(error: String) {
        context?.showToast(error)
    }

    override fun setData(bean: HotBean) {
        bean.itemList.forEach { list.add(it.data) }
        adapter.notifyDataSetChanged()
    }
}