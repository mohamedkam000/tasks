package com.app.tasks.utils

import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object SystemUtils {
    fun getAppVersion(context: Context): String {
        val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val verName = pkgInfo.versionName
        val verCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pkgInfo.longVersionCode.toInt()
        } else {
            pkgInfo.versionCode
        }
        return "$verName ($verCode)"
    }

    fun getTime(): String {
        val currentTime = Calendar.getInstance().time

        val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())

        return sdf.format(currentTime)
    }
}