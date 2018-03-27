package com.qyh.eyekotlin.network

import android.content.Context
import android.util.Log
import com.qyh.eyekotlin.network.interceptor.CacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  13:57
 *
 * @desc 获取Retrofit客户端, 单例模式
 *
 */
class RetrofitClient private constructor(context: Context, baseUrl: String) {
    /**
     * 缓存地址
     */
    private val httpCacheDirectory: File by lazy { File(context.cacheDir, "app_cache") }
    private val cache: Cache by lazy { Cache(httpCacheDirectory, 10 * 1024 * 1024) }
    /**
     * 创建OKHTTP客户端
     * 1. 设置在线缓存路径
     * 2. 添加请求日志拦截
     * 3. 添加在线缓存拦截
     * 4. 设置连接超时
     * 5. 设置写超时
     */
    private val okhttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(CacheInterceptor(context))
                .addInterceptor(CacheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()
    }
    /**
     * 创建Retrofit客户端
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }
    /**
     * 连接超时时间(秒)
     */
    private val DEFAULT_TIMEOUT: Long = 20

    companion object {
        var instance: RetrofitClient? = null

        fun init(context: Context, baseUrl: String) {
            if (instance == null) {
                synchronized(RetrofitClient::class) {
                    if (instance == null) {
                        instance = RetrofitClient(context, baseUrl)
                    }
                }
            }
        }
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}
