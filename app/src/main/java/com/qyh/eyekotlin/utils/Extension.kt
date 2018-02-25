package com.qyh.eyekotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author 邱永恒
 *
 * @time 2018/2/16  15:44
 *
 * @desc 函数扩展
 *
 */

/**
 * 显示吐司
 */
fun Context.showToast(message: String) : Toast {
    val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}

/**
 * 界面跳转
 * inline: 内联函数
 * reified: 具体对象
 */
inline fun <reified T: Activity> Activity.newIntent() {
    // T::class.java反射获取class对象
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

/**
 * RxJava扩展线程调度
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}


