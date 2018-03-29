package com.qyh.eyekotlin.model

import android.content.Context
import com.qyh.eyekotlin.model.bean.HotBean
import com.qyh.eyekotlin.network.RetrofitClient
import com.qyh.eyekotlin.network.api.ApiService
import io.reactivex.Observable
import zlc.season.rxdownload3.core.DownloadConfig.context

/**
 * @author 邱永恒
 *
 * @time 2018/3/29  17:32
 *
 * @desc ${TODD}
 *
 */
class RankModel {
    fun loadData(strategy: String?): Observable<HotBean>? {
        return RetrofitClient.instance?.create(ApiService::class.java)?.getHotData(10, strategy!!, "26868b32e808498db32fd51fb422d00175e179df", 83)
    }
}