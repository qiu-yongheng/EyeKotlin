package com.qyh.eyekotlin.utils.helper

import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/30  8:48
 *
 * @desc 权限帮助类
 *
 */
object PermissionHelper {
    /**
     * 存储权限和定位权限(在MainActivity申请)
     * @param listener
     */
    fun requestStorageAndLocationAndPhone(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.STORAGE, PermissionConstants.PHONE, PermissionConstants.LOCATION)
    }

    /**
     * 存储权限和拍照权限(在PointTagActivity申请)
     * @param listener
     */
    fun requestStorageAndCamera(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.STORAGE, PermissionConstants.CAMERA)
    }

    /**
     * 存储权限
     * @param listener 同意监听
     */
    fun requestStorage(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.STORAGE)
    }

    /**
     * 打电话权限
     * @param listener 同意监听
     */
    fun requestPhone(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.PHONE)
    }

    /**
     * 打电话权限
     * @param grantedListener 同意监听
     * @param deniedListener 拒绝监听
     */
    fun requestPhone(grantedListener: OnPermissionGrantedListener,
                     deniedListener: OnPermissionDeniedListener) {
        request(grantedListener, deniedListener, PermissionConstants.PHONE)
    }

    /**
     * 发短信权限
     * @param listener 同意监听
     */
    fun requestSms(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.SMS)
    }

    /**
     * 定位权限
     * @param listener 同意监听
     */
    fun requestLocation(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.LOCATION)
    }

    /**
     * 拍照权限
     * @param listener
     */
    fun requestCamera(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.CAMERA)
    }

    /**
     * 录音权限
     * @param listener
     */
    fun requestAudio(listener: OnPermissionGrantedListener) {
        request(listener, PermissionConstants.MICROPHONE)
    }

    /**
     * 请求权限
     * @param grantedListener 同意监听
     * @param permissions 需要申请的权限
     */
    private fun request(grantedListener: OnPermissionGrantedListener,
                        @PermissionConstants.Permission vararg permissions: String) {
        request(grantedListener, null, *permissions)
    }

    private fun request(grantedListener: OnPermissionGrantedListener?,
                        deniedListener: OnPermissionDeniedListener?,
                        @PermissionConstants.Permission vararg permissions: String) {
        PermissionUtils.permission(*permissions)
                .rationale { shouldRequest -> DialogHelper.showRationaleDialog(shouldRequest) }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: List<String>) {
                        grantedListener?.onPermissionGranted()
                        LogUtils.d(permissionsGranted)
                    }

                    override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog()
                        }
                        deniedListener?.onPermissionDenied()
                        LogUtils.d(permissionsDeniedForever, permissionsDenied)
                    }
                })
                .request()
    }

    interface OnPermissionGrantedListener {
        /**
         * 权限同意
         */
        fun onPermissionGranted()
    }

    interface OnPermissionDeniedListener {
        /**
         * 权限拒绝
         */
        fun onPermissionDenied()
    }
}