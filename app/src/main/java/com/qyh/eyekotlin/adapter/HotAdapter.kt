package com.qyh.eyekotlin.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.qyh.eyekotlin.R

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  14:26
 *
 * @desc 搜索热门词
 *
 */
class HotAdapter(layoutResId: Int, data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        val layoutParams = helper?.getView<TextView>(R.id.tv_hot)?.layoutParams
        if (layoutParams is FlexboxLayoutManager.LayoutParams) {
            layoutParams.flexGrow = 1.0f
        }
        helper?.setText(R.id.tv_hot, item)
    }
}