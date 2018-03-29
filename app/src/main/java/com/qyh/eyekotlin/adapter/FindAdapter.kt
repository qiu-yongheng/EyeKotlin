package com.qyh.eyekotlin.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.model.bean.FindBean
import com.qyh.eyekotlin.utils.ImageLoadUtils

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  8:38
 *
 * @desc ${TODD}
 *
 */
class FindAdapter(layoutResId: Int, data: MutableList<FindBean>?) : BaseQuickAdapter<FindBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: FindBean?) {
        val imageView = helper?.getView<ImageView>(R.id.iv_photo)
        ImageLoadUtils.display(mContext, imageView!!, item?.bgPicture!!)
        helper.setText(R.id.tv_title, item.name)
    }
}