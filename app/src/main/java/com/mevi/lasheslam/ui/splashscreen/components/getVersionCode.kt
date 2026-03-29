package com.mevi.lasheslam.ui.splashscreen.components

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getVersionCode(): Long{
    val context = LocalContext.current
    try {
        val packegeInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packegeInfo.longVersionCode
        } else {
            packegeInfo.versionCode.toLong()
        }
    }catch (e : PackageManager.NameNotFoundException){
        return 0
    }
}