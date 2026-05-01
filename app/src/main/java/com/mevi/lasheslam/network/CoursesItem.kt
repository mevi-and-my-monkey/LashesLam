package com.mevi.lasheslam.network

import java.util.Date

data class CoursesItem(
    val id: String,
    val titulo: String,
    val imagen: String,
    val costo: Double,
    val date: Date,
    val horaIncio: String,
    val horaFin: String,
)
