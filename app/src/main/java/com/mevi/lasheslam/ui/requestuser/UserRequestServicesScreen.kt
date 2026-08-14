package com.mevi.lasheslam.ui.requestuser

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.domain.model.ReservationStatus
import com.mevi.lasheslam.domain.model.ServiceReservation
import com.mevi.lasheslam.ui.theme.CormorantGaramond
import com.mevi.lasheslam.utils.Utilities

@Composable
fun UserRequestServicesScreen(
    reservations: List<ServiceReservation>,
    clabe: String? = null,
    onSendReceipt: (ServiceReservation) -> Unit = {}
) {
    if (reservations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no tienes reservaciones de servicios",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    LazyColumn {
        items(items = reservations, key = { it.reservationId }) { reservation ->
            RequestUserReservationItem(
                item = reservation,
                clabe = clabe,
                onSendReceipt = onSendReceipt
            )
        }
    }
}

@Composable
fun RequestUserReservationItem(
    item: ServiceReservation,
    clabe: String? = null,
    onSendReceipt: (ServiceReservation) -> Unit = {}
) {
    val isPendingDeposit = item.status == ReservationStatus.PENDING_DEPOSIT.value

    val (statusColor, statusBackground, statusText) = when (item.status) {
        ReservationStatus.PENDING_DEPOSIT.value ->
            Triple(Color(0xFFB07A1E), Color(0xFFFBF0D9), "Pendiente de anticipo")

        ReservationStatus.SCHEDULED.value ->
            Triple(Color(0xFF4E7044), Color(0xFFE8F0E5), "Agendado")

        ReservationStatus.CANCELLED.value ->
            Triple(Color(0xFFB23A48), Color(0xFFF7E4E6), "Cancelado")

        ReservationStatus.ARCHIVED.value ->
            Triple(Color(0xFF5B5B5B), Color(0xFFEDEDED), "Archivado")

        else ->
            Triple(Color(0xFF8B7355), Color(0xFFFAF3E7), "Pendiente")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(statusColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.serviceName.uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = CormorantGaramond,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontSize = 18.sp
                            ),
                            color = Color(0xFFD97D8C)
                        )
                        Text(
                            text = "#${item.reservationNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = Utilities.formatMoney(item.price),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = CormorantGaramond,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 22.sp
                        ),
                        color = Color(0xFF1C1C1C)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.dateLabel} · ${item.time} h · ${item.durationLabel}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusBackground)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            fontSize = 11.sp
                        ),
                        color = statusColor
                    )
                }

                if (isPendingDeposit) {
                    DepositSection(
                        deposit = item.deposit,
                        clabe = clabe,
                        onSendReceipt = { onSendReceipt(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DepositSection(
    deposit: Double,
    clabe: String?,
    onSendReceipt: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFBF0D9))
            .padding(16.dp)
    ) {
        Text(
            text = "Para confirmar tu cita realiza el anticipo y envía tu comprobante.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B5A2E)
        )

        if (deposit > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Anticipo: ${Utilities.formatMoney(deposit)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1C1C1C)
            )
        }

        if (!clabe.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CLABE interbancaria",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = clabe,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color(0xFF1C1C1C),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Copiar",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFD97D8C),
                    modifier = Modifier.clickable {
                        clipboard.setText(AnnotatedString(clabe))
                        Toast.makeText(context, "CLABE copiada", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSendReceipt,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97D8C))
        ) {
            Text(text = "Enviar comprobante", color = Color.White)
        }
    }
}
