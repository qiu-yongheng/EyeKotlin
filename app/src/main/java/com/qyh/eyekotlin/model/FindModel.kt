package com.qyh.eyekotlin.model

import com.qyh.eyekotlin.model.bean.FindBean
import com.qyh.eyekotlin.network.RetrofitClient
import com.qyh.eyekotlin.network.api.ApiService
import io.reactivex.Observable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  8:30
 *
 * @desc ${TODD}
 *
 */
class FindModel {
    fun loadData(): Observable<ArrayList<FindBean>>? {
        return RetrofitClient.instance?.create(ApiService::class.java)?.getFindData()
    }
}