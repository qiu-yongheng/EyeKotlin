package com.qyh.eyekotlin.model

import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.network.RetrofitClient
import com.qyh.eyekotlin.network.api.ApiService
import io.reactivex.Observable

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  10:33
 *
 * @desc ${TODD}
 *
 */
class FindDetailModel {
    fun loadData(start : Int, categoryName: String, strategy: String?): Observable<HotBean>? {
        return RetrofitClient.instance?.create(ApiService::class.java)?.getFindDetailMoreData(start,10,categoryName, strategy!!)
    }
}