package com.qyh.eyekotlin.utils.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.utils.showToast
import zlc.season.rxdownload3.core.DownloadConfig.context

/**
 * @author 邱永恒
 *
 * @time 2018/3/30  8:50
 *
 * @desc 弹窗帮助类
 *
 */
object DialogHelper {
    /**
     * 当权限被拒绝时, 弹窗
     *
     * @param shouldRequest
     */
    fun showRationaleDialog(shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("您已拒绝我们申请授权，请同意授权，否则该功能将无法正常使用")
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which -> shouldRequest.again(true) })
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which -> shouldRequest.again(false) })
                .setCancelable(false)
                .create()
                .show()

    }

    /**
     * 当权限被永久拒绝时, 提示到设置界面授权
     */
    fun showOpenAppSettingDialog() {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("我们需要一些您拒绝的权限或系统未能应用失败的权限，请手动在设置页面授权，否则该功能将无法正常使用!")
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    // 打开设置界面
                    PermissionUtils.openAppSettings()
                })
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which -> })
                .setCancelable(false)
                .create()
                .show()
    }

    /**
     * 显示赞赏弹窗
     */
    fun showDonateDialog() {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
                .setTitle(R.string.donate)
                .setMessage(R.string.donate_content)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    // 将指定账号添加到剪切板
                    // add the alipay account to clipboard
                    val manager = topActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("text", topActivity.getString(R.string.donate_account))
                    manager.primaryClip = clipData
                    topActivity.showToast("已复制到剪贴板")
                })
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which -> })
                .setCancelable(false)
                .create()
                .show()
    }
}