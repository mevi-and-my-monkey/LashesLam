package com.mevi.lasheslam.ui.profile.request

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.domain.model.DeliveryType
import com.mevi.lasheslam.domain.model.OrderStatus
import com.mevi.lasheslam.domain.model.ProductOrder
import com.mevi.lasheslam.utils.Utilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdmRequestProductsScreen(viewModel: AdminProductOrdersViewModel = hiltViewModel()) {
    val orders = viewModel.orders

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OrderFilterRow(
            selected = viewModel.filter,
            onSelect = { viewModel.onFilterSelected(it) }
        )

        if (orders.isEmpty() && !viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay órdenes de productos en este filtro",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                items(items = orders, key = { it.orderId }) { order ->
                    AdminProductOrderItem(
                        order = order,
                        onComplete = { viewModel.completeOrder(order) },
                        onStartDelivery = { viewModel.startDelivery(order) },
                        onMarkShipped = { viewModel.markShipped(order.orderId) },
                        onMarkDelivered = { viewModel.markDelivered(order.orderId) },
                        onArchive = { viewModel.archiveOrder(order.orderId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderFilterRow(
    selected: OrderFilter,
    onSelect: (OrderFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OrderFilter.entries.forEach { filter ->
            val isSelected = filter == selected
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
                    .clickable { onSelect(filter) }
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
}

@Composable
fun AdminProductOrderItem(
    order: ProductOrder,
    onComplete: () -> Unit,
    onStartDelivery: () -> Unit,
    onMarkShipped: () -> Unit,
    onMarkDelivered: () -> Unit,
    onArchive: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isPending = order.status == OrderStatus.PENDING.value
    val isArchived = order.status == OrderStatus.ARCHIVED.value
    val isDelivery = order.deliveryType == DeliveryType.DELIVERY.value

    val (statusColor, statusBackground, statusLabel) = when (order.status) {
        OrderStatus.PENDING.value -> Triple(Color(0xFF8B7355), Color(0xFFFAF3E7), "PENDIENTE")
        OrderStatus.PREPARING.value -> Triple(Color(0xFFB07A1E), Color(0xFFFBF0D9), "EN PREPARACIÓN")
        OrderStatus.SHIPPED.value -> Triple(Color(0xFF2A6DB0), Color(0xFFE1EDF7), "ENVIADO")
        OrderStatus.DELIVERED.value -> Triple(Color(0xFF4E7044), Color(0xFFE8F0E5), "ENTREGADO")
        OrderStatus.ARCHIVED.value -> Triple(Color(0xFF5B5B5B), Color(0xFFEDEDED), "ARCHIVADO")
        else -> Triple(Color(0xFF4E7044), Color(0xFFE8F0E5), "COMPLETADO")
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

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
                        text = "#${order.orderNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.nameUser,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = order.emailUser,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = Utilities.formatMoney(order.total),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null
                        )
                    }
                }
            }

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

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = dateFormat.format(Date(order.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    if (order.deliveryType.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isDelivery) "Envío a domicilio" else "Recoger en tienda",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (isDelivery && order.address.isNotBlank()) {
                            Text(
                                text = order.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity} x ${item.title}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = Utilities.formatMoney(item.price * item.quantity),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Subtotal",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = Utilities.formatMoney(order.subtotal),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (order.shipping > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Envío",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = Utilities.formatMoney(order.shipping),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isArchived) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Acción principal según tipo de entrega y estado actual
                            val primaryAction: Pair<String, () -> Unit>? = when {
                                isDelivery && order.status == OrderStatus.PENDING.value ->
                                    "Aceptar y preparar" to onStartDelivery
                                isDelivery && order.status == OrderStatus.PREPARING.value ->
                                    "Marcar enviado" to onMarkShipped
                                isDelivery && order.status == OrderStatus.SHIPPED.value ->
                                    "Marcar entregado" to onMarkDelivered
                                !isDelivery && isPending ->
                                    "Completado" to onComplete
                                else -> null
                            }

                            if (primaryAction != null) {
                                Button(
                                    onClick = primaryAction.second,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4E7044)
                                    ),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                        horizontal = 8.dp,
                                        vertical = 6.dp
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(primaryAction.first, color = Color.White, fontSize = 13.sp)
                                }
                            }
                            Button(
                                onClick = onArchive,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1C1C1C)
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                    horizontal = 8.dp,
                                    vertical = 6.dp
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Archivar", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
