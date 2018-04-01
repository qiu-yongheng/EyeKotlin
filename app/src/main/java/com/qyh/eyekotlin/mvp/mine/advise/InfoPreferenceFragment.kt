package com.qyh.eyekotlin.mvp.mine.advise

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.utils.helper.CustomTabsHelper
import com.qyh.eyekotlin.utils.helper.DialogHelper
import com.qyh.eyekotlin.utils.showToast

/**
 * @author 邱永恒
 *
 * @time 2018/4/1  09:14
 *
 * @desc ${TODO}
 *
 */

class InfoPreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.info_preference)

        // 在GitHub上关注我
        findPreference("follow_me_on_github").setOnPreferenceClickListener { p ->
            CustomTabsHelper.openUrl(context!!, getString(R.string.follow_me_on_github_desc))
            true
        }

        // 关注我的个人博客
        findPreference("follow_me_on_blog").setOnPreferenceClickListener { p ->
            CustomTabsHelper.openUrl(context!!, getString(R.string.github_blog_desc))
            true
        }

        // 源代码
        findPreference("source_code").setOnPreferenceClickListener { p ->
            CustomTabsHelper.openUrl(context!!, getString(R.string.source_code_desc))
            true
        }

        // 使用邮件发送反馈
        findPreference("feedback").setOnPreferenceClickListener { p ->
            try {
                val uri = Uri.parse(getString(R.string.sendto))
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic))
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.device_model) + Build.MODEL + "\n"
                                + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                + getString(R.string.version))
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException) {
                context?.showToast("没有安装邮件软件")
            }
            true
        }

        // 赞赏
        findPreference("coffee").setOnPreferenceClickListener { p ->
            DialogHelper.showDonateDialog()
            true
        }
    }
}