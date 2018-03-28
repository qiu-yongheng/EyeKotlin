package com.qyh.eyekotlin.network.api

import com.qyh.eyekotlin.model.bean.HomeBean
import com.qyh.eyekotlin.model.bean.HotBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  15:09
 *
 * @desc ${TODD}
 *
 */
interface ApiService {
    companion object {
        val BASE_URL: String
            get() = "http://baobab.kaiyanapp.com/api/"
    }

    /**
     * 获取首页第一页数据
     */
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    fun getHomeData(): Observable<HomeBean>

    /**
     * 获取首页第一页之后的数据  ?date=1499043600000&num=2
     */
    @GET("v2/feed")
    fun getHomeMoreData(@Query("date") date: String, @Query("num") num: String): Observable<HomeBean>

    /**
     * 获取关键词搜索相关信息
     */
    @GET("v1/search")
    fun getSearchData(@Query("num") num: Int, @Query("query") query: String,
                      @Query("start") start: Int): Observable<HotBean>
}