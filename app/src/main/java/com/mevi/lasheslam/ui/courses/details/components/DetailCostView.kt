package com.mevi.lasheslam.ui.courses.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun DetailCostView(
    costoTotal: String?,
    costoApartado: String?,
    horaFin: String,
    horaInicio: String
) {
    val duration = calculateDuration(horaInicio, horaFin)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CostItem(
            icon = Icons.Outlined.Payments,
            label = stringResource(R.string.total_cost),
            value = "$${costoTotal ?: "0"}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!costoApartado.isNullOrEmpty()){
            CostItem(
                icon = Icons.Outlined.AccountBalanceWallet,
                label = stringResource(R.string.apartado_det),
                value = "$${costoApartado}"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        CostItem(
            icon = Icons.Outlined.Timer,
            label = stringResource(R.string.horario),
            value = duration,
            subValue = "$horaInicio - $horaFin"
        )
    }
}

private fun calculateDuration(start: String, end: String): String {
    return try {
        val startParts = start.split(":")
        val endParts = end.split(":")
        if (startParts.size < 2 || endParts.size < 2) return "N/A"

        val startMin = startParts[0].trim().toInt() * 60 + startParts[1].trim().take(2).toInt()
        val endMin = endParts[0].trim().toInt() * 60 + endParts[1].trim().take(2).toInt()

        val diff = if (endMin >= startMin) endMin - startMin else (24 * 60 - startMin) + endMin
        val hours = diff / 60
        val mins = diff % 60

        if (hours > 0 && mins > 0) "${hours}h ${mins}m"
        else if (hours > 0) "${hours}h"
        else "${mins}m"
    } catch (e: Exception) {
        "N/A"
    }
}

@Preview(showBackground = true)
@Composable
fun DetailCostViewPreview() {
    LashesLamTheme {
        DetailCostView(
            costoTotal = "2500",
            costoApartado = "500",
            horaInicio = "09:00",
            horaFin = "14:00"
        )
    }
}
