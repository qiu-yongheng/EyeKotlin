package com.qyh.eyekotlin.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.Data
import com.qyh.eyekotlin.utils.ImageLoadUtils
import com.qyh.eyekotlin.utils.TransformUtils

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/28  10:38
 *
 * @desc ${TODD}
 *
 */
class FeedAdapter(layoutResId: Int, data: MutableList<Data>) : BaseQuickAdapter<Data, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: Data) {
        val ivPhoto = helper.getView<ImageView>(R.id.iv_photo)
        ImageLoadUtils.display(mContext, ivPhoto, item.cover.feed)
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_detail, "${item.category} / ${TransformUtils.playDuration(item.duration)} / ${TransformUtils.formatDate(item.date)}")
    }
}