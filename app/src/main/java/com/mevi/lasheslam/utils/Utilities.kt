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
import com.mevi.lasheslam.network.ProductOrder
import com.mevi.lasheslam.network.ServiceReservation
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

    fun formatMoney(value: Double): String {
        return if (value % 1.0 == 0.0) {
            "$${value.toInt()}"
        } else {
            "$" + String.format(Locale.getDefault(), "%.2f", value)
        }
    }

    fun createOrderMessageWhatsApp(order: ProductOrder, whatsapp: String): String {
        val itemsDetail = order.items.joinToString("\n") { item ->
            "• ${item.quantity} x ${item.title} — ${formatMoney(item.price * item.quantity)}"
        }

        val shippingLine = if (order.shipping > 0) {
            "\nEnvío: ${formatMoney(order.shipping)}"
        } else {
            ""
        }

        val message = """
        Hola, soy ${order.nameUser} y quiero realizar el siguiente pedido:
        Orden: #${order.orderNumber}

        $itemsDetail

        Subtotal: ${formatMoney(order.subtotal)}$shippingLine
        Total: ${formatMoney(order.total)}

        ¡Gracias!
        """.trimIndent()

        val encodedMessage = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8.toString()
        )
        return "https://wa.me/${whatsapp}?text=$encodedMessage"
    }

    fun createReservationMessageWhatsApp(
        reservation: ServiceReservation,
        whatsapp: String
    ): String {
        val message = """
        Hola, soy ${reservation.nameUser} y quiero reservar una cita:
        Reserva: #${reservation.reservationNumber}

        Servicio: ${reservation.serviceName}
        Fecha: ${reservation.dateLabel}
        Hora: ${reservation.time}
        Duración: ${reservation.durationLabel}
        Total: ${formatMoney(reservation.price)}

        Quedo al pendiente de los datos para el anticipo. ¡Gracias!
        """.trimIndent()

        val encodedMessage = URLEncoder.encode(
            message,
            StandardCharsets.UTF_8.toString()
        )
        return "https://wa.me/${whatsapp}?text=$encodedMessage"
    }

    fun formatServiceDuration(duration: Double): String {
        val hours = duration.toInt()
        val minutes = ((duration - hours) * 60).toInt()
        return when {
            hours > 0 && minutes > 0 -> "$hours h $minutes"
            hours > 0 -> "$hours h"
            else -> "$minutes min"
        }
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