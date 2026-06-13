package com.mevi.lasheslam.ui.profile.request

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.network.ServiceReservation
import com.mevi.lasheslam.utils.Utilities
import java.util.Calendar
import java.util.Locale

private val PendingColor = Color(0xFF8B7355)
private val ScheduledColor = Color(0xFF4E7044)
private val CancelledColor = Color(0xFFB23A48)
private val ArchivedColor = Color(0xFF5B5B5B)

@Composable
fun AdmRequestServicesScreen(viewModel: AdminReservationsViewModel = hiltViewModel()) {
    var showAvailabilityEditor by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadReservations()
        viewModel.loadAvailability()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Filtro de status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReservationFilter.entries.forEach { filter ->
                val isSelected = filter == viewModel.filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            }
                        )
                        .clickable { viewModel.onFilterSelected(filter) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = filter.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.DarkGray
                    )
                }
            }
        }

        // Opción de horarios, aparte del filtro
        OutlinedButton(
            onClick = { showAvailabilityEditor = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Gestionar días y horarios")
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (viewModel.reservations.isEmpty() && !viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay reservaciones en este filtro",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                items(items = viewModel.reservations, key = { it.reservationId }) { reservation ->
                    AdminReservationItem(
                        reservation = reservation,
                        onAccept = { viewModel.acceptReservation(reservation.reservationId) },
                        onReject = { viewModel.rejectReservation(reservation.reservationId) },
                        onArchive = { viewModel.archiveReservation(reservation.reservationId) }
                    )
                }
            }
        }
    }

    if (showAvailabilityEditor) {
        AvailabilityEditorSheet(
            viewModel = viewModel,
            onDismiss = { showAvailabilityEditor = false }
        )
    }
}

@Composable
fun AdminReservationItem(
    reservation: ServiceReservation,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onArchive: () -> Unit
) {
    val (statusColor, statusBackground, statusLabel) = when (reservation.status) {
        FirestorePaths.Booking.STATUS_PENDING ->
            Triple(PendingColor, Color(0xFFFAF3E7), "PENDIENTE")

        FirestorePaths.Booking.STATUS_SCHEDULED ->
            Triple(ScheduledColor, Color(0xFFE8F0E5), "AGENDADO")

        FirestorePaths.Booking.STATUS_CANCELLED ->
            Triple(CancelledColor, Color(0xFFF7E4E6), "CANCELADO")

        else ->
            Triple(ArchivedColor, Color(0xFFEDEDED), "ARCHIVADO")
    }

    val isPending = reservation.status == FirestorePaths.Booking.STATUS_PENDING
    val isArchived = reservation.status == FirestorePaths.Booking.STATUS_ARCHIVED

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "#${reservation.reservationNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = reservation.serviceName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = reservation.nameUser,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = reservation.emailUser,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = Utilities.formatMoney(reservation.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
                    text = "${reservation.dateLabel} · ${reservation.time} h · " +
                            reservation.durationLabel,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        fontSize = 11.sp
                    ),
                    color = statusColor
                )
            }

            if (!isArchived) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isPending) {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = ScheduledColor),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Aceptar", color = Color.White, fontSize = 12.sp)
                        }
                        Button(
                            onClick = onReject,
                            colors = ButtonDefaults.buttonColors(containerColor = CancelledColor),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Rechazar", color = Color.White, fontSize = 12.sp)
                        }
                    }
                    Button(
                        onClick = onArchive,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1C)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Archivar", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailabilityEditorSheet(
    viewModel: AdminReservationsViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val selectedDate = viewModel.selectedEditorDate
    val slots = viewModel.slotsForSelectedDate()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp)
        ) {
            Text(
                text = "Disponibilidad de citas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cada servicio tiene sus propios días y horarios.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de servicio
            Text(
                text = "SERVICIO",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.services.forEach { service ->
                    val isSelected = service.id == viewModel.selectedServiceId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                }
                            )
                            .clickable { viewModel.selectService(service.id) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = service.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "FECHA",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Selector de fecha (próximos días)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.selectableDays.forEach { day ->
                    val isSelected = day.isoDate == selectedDate
                    val hasSlots = viewModel.dayHasSlots(day.isoDate)
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) Color(0xFFD97D8C) else Color(0xFFF3F3F3)
                            )
                            .clickable { viewModel.selectEditorDate(day.isoDate) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White else Color.Gray
                        )
                        Text(
                            text = day.dayNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                        Text(
                            text = day.monthLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = if (isSelected) Color.White else Color.Gray
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (hasSlots) {
                                        if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.Transparent
                                    }
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "HORARIOS DEL DÍA",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (selectedDate == null) return@Button
                    val now = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            viewModel.addSlot(String.format(Locale.US, "%02d:%02d", hour, minute))
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        0,
                        true
                    ).show()
                },
                enabled = selectedDate != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar horario", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (slots.isEmpty()) {
                Text(
                    text = "Sin horarios para este día",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                slots.forEach { slot ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (slot.occupied) {
                                    Color(0xFFEDEDED)
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                }
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = slot.time,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (slot.occupied) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            ),
                            fontWeight = FontWeight.Bold,
                            color = if (slot.occupied) Color.Gray else Color.Black
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (slot.occupied) {
                                            ScheduledColor.copy(alpha = 0.15f)
                                        } else {
                                            Color(0xFFFAF3E7)
                                        }
                                    )
                                    .clickable { viewModel.toggleSlotOccupied(slot.time) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (slot.occupied) "Liberar" else "Ocupar",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (slot.occupied) ScheduledColor else PendingColor
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar horario",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { viewModel.removeSlot(slot.time) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveAvailability { success ->
                        if (success) onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Guardar disponibilidad", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
