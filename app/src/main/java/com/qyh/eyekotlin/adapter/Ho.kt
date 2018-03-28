package com.qyh.eyekotlin.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.HomeBean
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.mvp.videodetail.VideoDetailActivity
import com.qyh.eyekotlin.utils.ImageLoadUtils
import com.qyh.eyekotlin.utils.ObjectSaveUtils
import com.qyh.eyekotlin.utils.SPUtils

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  11:27
 *
 * @desc ${TODD}
 *
 */
class Ho(private var context: Context, private var list: ArrayList<HomeBean.IssueListBean.ItemListBean>) : RecyclerView.Adapter<Ho.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(inflater.inflate(R.layout.item_home, parent, false), context)
    }

    private var inflater = LayoutInflater.from(context)
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val bean = list[position]
        val title = bean.data?.title // 标题
        val category = bean.data?.category // 类别
        val minute = bean.data?.duration?.div(60) // 分钟
        val second = bean.data?.duration?.minus((minute?.times(60)) as Long) // 秒
        val realMinute: String
        val realSecond: String
        realMinute = if (minute!! < 10) {
            "0" + minute
        } else {
            minute.toString()
        }
        realSecond = if (second!! < 10) {
            "0" + second
        } else {
            second.toString()
        }

        var playUrl = bean.data?.playUrl // 播放路径
        val photo = bean.data?.cover?.feed // 图片路径
        val author = bean.data?.author // 作者图片路径
        ImageLoadUtils.display(context, holder?.iv_photo!!, photo as String) // 显示图片
        holder.tv_title?.text = title
        holder.tv_detail?.text = "发布于 $category / $realMinute:$realSecond"
        if (author != null) {
            ImageLoadUtils.display(context, holder.iv_user!!, author.icon as String)
        } else {
            holder.iv_user?.visibility = View.GONE
        }
        holder.itemView?.setOnClickListener {
            //跳转视频详情页
            val intent: Intent = Intent(context, VideoDetailActivity::class.java)
            val desc = bean.data?.description
            val duration = bean.data?.duration
            val playUrl = bean.data?.playUrl
            val blurred = bean.data?.cover?.blurred
            val collect = bean.data?.consumption?.collectionCount
            val share = bean.data?.consumption?.shareCount
            val reply = bean.data?.consumption?.replyCount
            val time = System.currentTimeMillis()
            val videoBean = VideoBean(photo, title, desc, duration, playUrl, category, blurred, collect, share, reply, time)
            val url = SPUtils.getInstance(context, "beans").getString(playUrl!!)
            if (TextUtils.isEmpty(url)) {
                var count = SPUtils.getInstance(context, "beans").getInt("count")
                count = if (count != -1) {
                    count.inc()
                } else {
                    1
                }
                SPUtils.getInstance(context, "beans").put("count", count)
                SPUtils.getInstance(context, "beans").put(playUrl, playUrl)
                ObjectSaveUtils.saveObject(context, "bean$count", videoBean)
            }
            val bundle = Bundle()
            bundle.putParcelable(VideoDetailActivity.VIDEO_DATA, videoBean)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    class HomeViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var tv_detail: TextView? = null
        var tv_title: TextView? = null
        var tv_time: TextView? = null
        var iv_photo: ImageView? = null
        var iv_user: ImageView? = null

        init {
            tv_detail = itemView.findViewById(R.id.tv_detail)
            tv_title = itemView.findViewById(R.id.tv_title)
            iv_photo = itemView.findViewById(R.id.iv_photo)
            iv_user = itemView.findViewById(R.id.iv_user)
            tv_title?.typeface = Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        }
   }
}