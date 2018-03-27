package com.qyh.eyekotlin.network.interceptor

import android.content.Context
import android.util.Log
import com.qyh.eyekotlin.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.file.attribute.AclEntry.newBuilder

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  14:34
 *
 * @desc 离线缓存
 *       1. max-age = 0, 在线不缓存, 如果不为0, 在有网的情况下, 会根据你设置的max-age时间内, 读缓存, 超过max-age后重新请求
 *       2. max-stale 设置离线缓存失效时间
 */
class CacheInterceptor(var context: Context) : Interceptor {
    companion object {
        const val TAG = "CacheInterceptor"
        // 最大离线缓存时间4周（秒）
        const val MAX_AGE_OFFLINE = 28 * 24 * 60 * 60
        // 在线缓存60秒, 在6秒内, 请求同一个接口, 读缓存
        const val MAX_AGE = 6
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (NetworkUtils.isNetConnected(context)) {
            val response = chain.proceed(request!!)
            val cacheControl = request.cacheControl().toString()
            Log.e(TAG, "6s读取缓存" + cacheControl)
            response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + MAX_AGE)
                    .build()
        } else {
            Log.e(TAG, "没有连接WiFi或数据, 读取缓存")
            // 无网的时候强制使用缓存
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            val response = chain.proceed(request)
            response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_AGE_OFFLINE)
                    .build()
        }
    }
}