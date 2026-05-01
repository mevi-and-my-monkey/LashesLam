package com.mevi.lasheslam.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toUiFormat(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(this)
}