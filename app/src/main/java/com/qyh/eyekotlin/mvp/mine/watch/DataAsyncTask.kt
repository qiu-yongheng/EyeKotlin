package com.qyh.eyekotlin.mvp.mine.watch

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import com.qyh.eyekotlin.model.bean.VideoBean
import com.qyh.eyekotlin.utils.ObjectSaveUtils
import com.qyh.eyekotlin.utils.SPUtils
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/29  22:19
 *
 * @desc ${TODO}
 *
 */

class DataAsyncTask(val handler: WeakReference<Handler>, val context: WeakReference<Context>) : AsyncTask<Void, Void, ArrayList<VideoBean>>() {
    private val list = ArrayList<VideoBean>()
    override fun doInBackground(vararg params: Void?): ArrayList<VideoBean> {
        val count = SPUtils.getInstance(context.get()!!, "beans").getInt("count")
        for (i in 1..count) {
            val bean :VideoBean= ObjectSaveUtils.getValue(context.get()!!, "bean$i") as VideoBean
            list.add(bean)
        }
        return list
    }

    override fun onPostExecute(result: ArrayList<VideoBean>?) {
        super.onPostExecute(result)
        var message = handler.get()?.obtainMessage()
        var bundle = Bundle()
        bundle.putParcelableArrayList("beans",result)
        message?.data = bundle
        handler.get()?.sendMessage(message)
    }

}