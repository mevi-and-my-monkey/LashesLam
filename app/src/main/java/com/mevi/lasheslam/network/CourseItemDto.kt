package com.mevi.lasheslam.network

import java.text.SimpleDateFormat
import java.util.Locale

data class CourseItemDto(
    val id: String = "",
    val titulo: String = "",
    val imagen: String = "",
    val costo: String = "",
    val fecha: String = "",
    val horaIncio: String = "",
    val horaFin: String = "",
)

fun CourseItemDto.toDomain(): CoursesItem? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val parsedDate = formatter.parse(fecha) ?: return null
        val parsedCosto = costo.toDoubleOrNull() ?: 0.0

        CoursesItem(
            id = id,
            titulo = titulo,
            imagen = imagen,
            costo = parsedCosto,
            date = parsedDate,
            horaIncio = horaIncio,
            horaFin = horaFin
        )
    } catch (e: Exception) {
        null
    }
}