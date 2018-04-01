package com.qyh.eyekotlin.app

import android.app.Application
import android.preference.PreferenceManager
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.blankj.utilcode.util.Utils
import com.qyh.eyekotlin.network.RetrofitClient
import com.qyh.eyekotlin.network.api.ApiService
import zlc.season.rxdownload3.core.DownloadConfig
import zlc.season.rxdownload3.extension.ApkInstallExtension
import zlc.season.rxdownload3.http.OkHttpClientFactoryImpl
import zlc.season.rxdownload3.notification.NotificationFactoryImpl

/**
 * @author 邱永恒
 *
 * @time 2018/2/27  9:45
 *
 * @desc ${TODD}
 *
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        initDownload()
        initRetrofit()
        initUtils()

    }

    private fun initDownload() {
        DownloadConfig.Builder.create(this)
                //设置更新频率
                .setFps(20)
                //自动开始下载
                .enableAutoStart(true)
                //启用数据库
                .enableDb(true)
                //启用Service
                .enableService(true)
                //启用Notification
                .enableNotification(true)
                //自定义通知
                .setNotificationFactory(NotificationFactoryImpl())
                //自定义OKHTTP
                .setOkHttpClientFacotry(OkHttpClientFactoryImpl())
                //添加扩展
                .addExtension(ApkInstallExtension::class.java)
    }

    private fun initRetrofit() {
        RetrofitClient.init(this, ApiService.BASE_URL)
    }

    private fun initUtils() {
        Utils.init(this)
    }
}
