package com.mevi.lasheslam.ui.splashscreen.components

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getVersion(): String {
    val context = LocalContext.current
    return try {
        val packegeInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packegeInfo.versionName ?: "1.0.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "Version no encontrada"
    }
}