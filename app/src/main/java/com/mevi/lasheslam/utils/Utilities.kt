package com.mevi.lasheslam.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
        context: Context,
        titulo: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ) {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val inicioDate = inputFormat.parse("$fecha $horaInicio") ?: return
        val finDate = inputFormat.parse("$fecha $horaFin") ?: return

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, titulo)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicioDate.time)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finDate.time)
            putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val inicioUTC = outputFormat.format(inicioDate)
        val finUTC = outputFormat.format(finDate)

        val calendarUrl = "https://calendar.google.com/calendar/render?action=TEMPLATE" +
                "&text=${Uri.encode(titulo)}" +
                "&dates=$inicioUTC/$finUTC"

        val webIntent = Intent(Intent.ACTION_VIEW, calendarUrl.toUri())

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                context.startActivity(webIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "No se puede abrir el calendario", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun openGoogleMaps(context: Context, lat: Double, lng: Double) {
        val uri = "geo:$lat,$lng?q=$lat,$lng".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

    fun validateRequired(value: String, message: String): Resource.Error? {
        return if (value.isBlank()) {
            Resource.Error(AppError.Unknown(message))
        } else {
            null
        }
    }

    fun createMessageWhatsApp(titulo: String, fecha: String, horaInicio: String, horaFin: String, whatsapp: String) : String {
        val message = """
        Hola, me gustaría recibir más información sobre el curso ${titulo}.
        Horario: $horaInicio - ${horaFin}.
        Fecha: ${fecha}.
        ¡Gracias!
        """.trimIndent()

        val encodedMessage = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8.toString()
        )
        return "https://wa.me/${whatsapp}?text=$encodedMessage"
    }

    fun createProductMessageWhatsApp(titulo: String, precio: String, whatsapp: String) : String {
        val message = """
        Hola, me gustaría recibir más información sobre el producto ${titulo}.
        Precio: ${precio}.
        ¡Gracias!
        """.trimIndent()

        val encodedMessage = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8.toString()
        )
        return "https://wa.me/${whatsapp}?text=$encodedMessage"
    }

    fun createServiceMessageWhatsApp(titulo: String, precio: String, whatsapp: String) : String {
        val message = """
        Hola, me gustaría recibir más información sobre el servicio $titulo y horarios disponibles.
        Precio: ${precio}.
        ¡Gracias!
        """.trimIndent()

        val encodedMessage = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8.toString()
        )
        return "https://wa.me/${whatsapp}?text=$encodedMessage"
    }
}