package com.mevi.lasheslam.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import androidx.core.net.toUri

object Utilities {

    suspend fun isAdmin(email: String): Boolean {
        return try {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate().await()
            val jsonString = remoteConfig.getString("list_admin")
            val list = try {
                val jsonArray = JSONArray(jsonString)
                List(jsonArray.length()) { i -> jsonArray.getString(i) }
            } catch (e: Exception) {
                emptyList()
            }
            list.contains(email)
        } catch (e: Exception) {
            false
        }
    }

    fun agregarEventoCalendario(
        navController: NavHostController,
        titulo: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ) {
        val context = navController.context

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val inicioMillis = formatter.parse("$fecha $horaInicio")?.time ?: return
        val finMillis = formatter.parse("$fecha $horaFin")?.time ?: return

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, titulo)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicioMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finMillis)
            putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        context.startActivity(intent)
    }

    fun openGoogleMaps(context: Context, lat: Double, lng: Double) {
        val uri = "geo:$lat,$lng?q=$lat,$lng".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }


}