package com.qyh.eyekotlin.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.utils.ImageLoadUtils
import com.qyh.eyekotlin.utils.TransformUtils

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  22:34
 *
 * @desc ${TODO}
 *
 */

class WatchAdapter(layoutResId: Int, data: MutableList<VideoBean>?) : BaseQuickAdapter<VideoBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: VideoBean?) {
        val imageView = helper?.getView<ImageView>(R.id.iv_photo)
        ImageLoadUtils.display(mContext, imageView!!, item?.feed!!)
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_time, "${item.category}/${TransformUtils.time(item.duration?.toInt()!!)}")
    }
}