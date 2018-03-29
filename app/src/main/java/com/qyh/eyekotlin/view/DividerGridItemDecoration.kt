package com.qyh.eyekotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


/**
 * @author 邱永恒
 *
 * @time 2018/3/29  9:41
 *
 * @desc ${TODD}
 *
 */
class DividerGridItemDecoration(context: Context, val divider: Drawable) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {

            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                    .spanCount
        }
        return spanCount
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = (child.right + params.rightMargin
                    + divider.intrinsicWidth)
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicWidth

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private fun isLastColumn(parent: RecyclerView, pos: Int, spanCount: Int,
                             childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)
            // 如果是最后一列，则不需要绘制右边
            {
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)
                // 如果是最后一列，则不需要绘制右边
                {
                    return true
                }
            } else {
                childCount -= childCount % spanCount
                if (pos >= childCount)
                    // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    private fun isLastRaw(parent: RecyclerView, pos: Int, spanCount: Int,
                          childCount: Int): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            childCount -= childCount % spanCount
            if (pos >= childCount)
            // 如果是最后一行，则不需要绘制底部
                return true
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount -= childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int,
                                parent: RecyclerView) {
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter.itemCount
        if (isLastRaw(parent, itemPosition, spanCount, childCount))
        // 如果是最后一行，则不需要绘制底部
        {
            outRect.set(0, 0, divider.intrinsicWidth, 0)
        } else if (isLastColumn(parent, itemPosition, spanCount, childCount))
        // 如果是最后一列，则不需要绘制右边
        {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider.intrinsicWidth,
                    divider.intrinsicHeight)
        }
    }
}