package com.qyh.eyekotlin.utils.helper

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils

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
}