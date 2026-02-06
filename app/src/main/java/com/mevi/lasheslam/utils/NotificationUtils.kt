package com.mevi.lasheslam.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mevi.lasheslam.R

private const val CHANNEL_ID = "courses_notifications"

fun showNotification(
    context: Context,
    title: String,
    message: String
) {
    // 🔐 ANDROID 13+ → verificar permiso
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            // Permiso no concedido → salimos sin crash
            return
        }
    }

    createNotificationChannel(context)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_ll_logo)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    NotificationManagerCompat.from(context)
        .notify(System.currentTimeMillis().toInt(), notification)
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Recordatorios de cursos",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de cursos aceptados"
        }

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}