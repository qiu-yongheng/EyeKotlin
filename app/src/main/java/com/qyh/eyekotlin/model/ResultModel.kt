package com.qyh.eyekotlin.model

import android.content.Context
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.network.RetrofitClient
import com.qyh.eyekotlin.network.api.ApiService
import io.reactivex.Observable

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  14:14
 *
 * @desc ${TODD}
 *
 */
class ResultModel {
    fun loadData(context: Context, query: String, start: Int): Observable<HotBean>? {
        return RetrofitClient.instance?.create(ApiService::class.java)?.getSearchData(10, query, start)
    }
}