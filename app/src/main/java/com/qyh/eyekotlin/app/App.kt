package com.qyh.eyekotlin.app

import android.app.Application
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
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        DownloadConfig.Builder.create(this)
                .setFps(20)                         //设置更新频率
                .enableAutoStart(true)              //自动开始下载
                .enableDb(true)                             //启用数据库
                .enableService(true)                        //启用Service
                .enableNotification(true)                   //启用Notification
                .setNotificationFactory(NotificationFactoryImpl()) 	    //自定义通知
                .setOkHttpClientFacotry(OkHttpClientFactoryImpl()) 	    //自定义OKHTTP
                .addExtension(ApkInstallExtension::class.java)          //添加扩展
    }
}