package com.qyh.eyekotlin.adapter

import android.util.Log
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.HomeBean
import com.qyh.eyekotlin.utils.ImageLoadUtils
import com.qyh.eyekotlin.utils.TransformUtils

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/28  22:02
 *
 * @desc ${TODO}
 *
 */

class HomeAdapter(layoutResId: Int, data: MutableList<HomeBean.IssueListBean.ItemListBean>?) : BaseQuickAdapter<HomeBean.IssueListBean.ItemListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: HomeBean.IssueListBean.ItemListBean) {
        val ivPhoto = helper.getView<ImageView>(R.id.iv_photo)
        val ivUser = helper.getView<ImageView>(R.id.iv_user)
        ImageLoadUtils.display(mContext, ivPhoto!!, item.data?.cover?.feed!!)
        ImageLoadUtils.display(mContext, ivUser!!, item.data?.author?.icon!!)
        helper.setText(R.id.tv_title, item.data?.title)
                .setText(R.id.tv_detail, "发布于 ${item.data?.category} / ${TransformUtils.playDuration(item.data?.duration?.toInt()!!)}")
    }
}