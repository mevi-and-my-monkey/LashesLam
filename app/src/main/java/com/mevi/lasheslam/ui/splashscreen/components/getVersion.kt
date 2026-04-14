package com.mevi.lasheslam.ui.splashscreen.components

import android.content.Context
import android.content.pm.PackageManager

fun getVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "Version no encontrada"
    }
}