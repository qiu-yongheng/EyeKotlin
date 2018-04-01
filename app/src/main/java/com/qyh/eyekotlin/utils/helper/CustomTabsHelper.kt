package com.qyh.eyekotlin.utils.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.utils.SPUtils
import zlc.season.rxdownload3.core.DownloadConfig
import zlc.season.rxdownload3.core.DownloadConfig.context

/**
 * @author 邱永恒
 *
 * @time 2018/4/1  10:03
 *
 * @desc ${TODO}
 *
 */

object CustomTabsHelper {
    /**
     * 打开链接
     */
    fun openUrl(context: Context, url: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (sharedPreferences.getBoolean("chrome_custom_tabs", true)) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            builder.build().launchUrl(context, Uri.parse(url))
        } else {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, R.string.no_browser_found, Toast.LENGTH_SHORT).show()
            }

        }
    }
}