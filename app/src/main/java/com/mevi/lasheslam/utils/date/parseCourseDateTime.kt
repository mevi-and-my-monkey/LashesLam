package com.mevi.lasheslam.utils.date

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CourseDateParser @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun parse(date: String, schedule: String): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val localDate = LocalDate.parse(date, dateFormatter)
        val startTime = schedule.split("-")[0].trim()
        val localTime = LocalTime.parse(startTime, timeFormatter)

        return LocalDateTime.of(localDate, localTime)
    }
}