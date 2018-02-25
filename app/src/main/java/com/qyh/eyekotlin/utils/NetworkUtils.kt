package com.qyh.eyekotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author 邱永恒
 *
 * @time 2018/2/25  14:35
 *
 * @desc ${TODD}
 *
 */
object NetworkUtils {
    fun isNetConnected(context: Context): Boolean {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectManager.activeNetworkInfo
        return if (networkInfo == null) {
            false
        } else {
            networkInfo.isAvailable && networkInfo.isConnected
        }

    }

    private fun isNetworkConnected(context: Context, typeMobile: Int): Boolean {
        if (!isNetConnected(context)) {
            return false
        }
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectManager.getNetworkInfo(typeMobile)
        return if (networkInfo == null) {
            false
        } else {
            networkInfo.isConnected && networkInfo.isAvailable
        }
    }

    /**
     * 判断是否连接数据
     */
    fun isPhoneNetConnected(context: Context): Boolean {
        val typeMobile = ConnectivityManager.TYPE_MOBILE
        return isNetworkConnected(context, typeMobile)
    }

    /**
     * 判断是否连接WiFi
     */
    fun isWifiNetConnected(context: Context): Boolean {
        val typeMobile = ConnectivityManager.TYPE_WIFI
        return isNetworkConnected(context, typeMobile)
    }
}