package com.qyh.eyekotlin.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author 邱永恒
 *
 * @time 2018/3/27  16:11
 *
 * @desc 显示隐藏键盘工具类
 *
 */
object KeyBoardUtils {

    fun openKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun closeKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

}