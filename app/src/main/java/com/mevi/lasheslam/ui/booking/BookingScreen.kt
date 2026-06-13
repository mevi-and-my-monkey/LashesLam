package com.mevi.lasheslam.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.theme.CormorantGaramond
import com.mevi.lasheslam.utils.Utilities

private val GoldAccent = Color(0xFFB08A3E)
private val NoticeBackground = Color(0xFFFAF3E7)

@Composable
fun BookingScreen(
    serviceId: String,
    onBackToHome: () -> Unit,
    popBack: () -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    LaunchedEffect(serviceId) {
        viewModel.load(serviceId)
    }

    val reservation = viewModel.reservationPlaced

    Box(modifier = Modifier.fillMaxSize()) {
        if (reservation != null) {
            BookingConfirmationView(
                viewModel = viewModel,
                onBackToHome = onBackToHome
            )
        } else {
            BookingContent(
                viewModel = viewModel,
                popBack = popBack,
                onOpenWhatsApp = onOpenWhatsApp
            )
        }

        GenericLoading(
            isLoading = viewModel.isLoading,
            message = stringResource(R.string.loading_generic),
            modifier = Modifier.fillMaxSize()
        )
    }

    if (viewModel.showError) {
        ErrorDialog(
            message = stringResource(R.string.booking_create_error),
            onDismiss = { viewModel.clearError() },
            onCancel = {}
        )
    }
}

@Composable
private fun BookingContent(
    viewModel: BookingViewModel,
    popBack: () -> Unit,
    onOpenWhatsApp: (String) -> Unit
) {
    val service = viewModel.service
    val canConfirm = viewModel.selectedDate != null && viewModel.selectedTime != null

    Column(modifier = Modifier.fillMaxSize()) {

        // Header rosa con back + título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                    ),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { popBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.book_appointment),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 22.sp
                    ),
                    color = Color.Black
                )
                if (service != null) {
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Card resumen del servicio
            if (service != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = service.image.ifEmpty { R.drawable.ic_guest },
                            contentDescription = service.title,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = service.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = CormorantGaramond,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 17.sp
                                ),
                                color = Color.Black
                            )
                            Text(
                                text = "${Utilities.formatServiceDuration(service.duration)} · " +
                                        Utilities.formatMoney(service.price),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (viewModel.availableDays.isEmpty() && !viewModel.isLoading) {
                Text(
                    text = stringResource(R.string.no_availability),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            } else {
                // Selector de día
                val monthLabel = (viewModel.selectedDate ?: viewModel.availableDays.firstOrNull())
                    ?.monthLabel.orEmpty()

                SectionLabel("${stringResource(R.string.choose_day).uppercase()} · $monthLabel")

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = viewModel.availableDays, key = { it.isoDate }) { day ->
                        DayPill(
                            day = day,
                            isSelected = viewModel.selectedDate?.isoDate == day.isoDate,
                            onClick = { viewModel.onDateSelected(day) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionLabel(stringResource(R.string.available_times).uppercase())

                Spacer(modifier = Modifier.height(8.dp))

                SlotsGrid(viewModel)

                Spacer(modifier = Modifier.height(20.dp))

                // Aviso de anticipo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NoticeBackground)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.booking_deposit_notice),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B5B3E)
                    )
                }
            }
        }

        // Botón inferior
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Button(
                onClick = { viewModel.confirmReservation(onOpenWhatsApp) },
                enabled = canConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(
                    text = stringResource(
                        if (canConfirm) R.string.confirm_booking else R.string.choose_date_time
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )
}

@Composable
private fun DayPill(
    day: BookingDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Color(0xFFD97D8C) else Color.White
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.dayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Color.White else Color.Gray
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = day.dayNumber,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
private fun SlotsGrid(viewModel: BookingViewModel) {
    val slots = viewModel.slots
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        slots.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { slot ->
                    val isTaken = slot in viewModel.takenSlots
                    val isSelected = viewModel.selectedTime == slot
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                when {
                                    isSelected -> Color(0xFFD97D8C)
                                    else -> Color.White
                                }
                            )
                            .clickable(enabled = !isTaken) { viewModel.onTimeSelected(slot) }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot,
                            style = MaterialTheme.typography.labelLarge.copy(
                                textDecoration = if (isTaken) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            ),
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isSelected -> Color.White
                                isTaken -> Color.LightGray
                                else -> Color.Black
                            }
                        )
                    }
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BookingConfirmationView(
    viewModel: BookingViewModel,
    onBackToHome: () -> Unit
) {
    val reservation = viewModel.reservationPlaced ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.6f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.booking_confirmed_badge),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.booking_thanks),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.booking_confirmed_message),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.summary).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Text(
                        text = "#${reservation.reservationNumber}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = reservation.serviceName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    color = Color.Black
                )
                Text(
                    text = "${reservation.dateLabel} · ${reservation.time} h · " +
                            reservation.durationLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = Utilities.formatMoney(reservation.price),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = CormorantGaramond,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackToHome,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text(
                text = stringResource(R.string.back_to_home),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
