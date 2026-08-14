package com.mevi.lasheslam.ui.cart

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.model.CartItem
import com.mevi.lasheslam.domain.model.DeliveryType
import com.mevi.lasheslam.domain.model.ProductOrder
import com.mevi.lasheslam.ui.components.AddressFormFields
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.theme.CormorantGaramond
import com.mevi.lasheslam.utils.InputValidator
import com.mevi.lasheslam.utils.Utilities

private val WhatsAppGreen = Color(0xFF25D366)
private val GoldAccent = Color(0xFFB08A3E)

@Composable
fun CartScreen(
    onBackToHome: () -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val orderPlaced = viewModel.orderPlaced
    var showDeliverySheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (orderPlaced != null) {
            OrderConfirmationView(
                order = orderPlaced,
                onBackToHome = {
                    viewModel.resetOrder()
                    onBackToHome()
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                CartHeader(onBackToHome = onBackToHome)

                if (items.isEmpty()) {
                    EmptyCartView()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                    ) {
                        items(items = items, key = { it.productId }) { item ->
                            CartItemCard(
                                item = item,
                                onQuantityChange = { quantity ->
                                    viewModel.updateQuantity(item.productId, quantity)
                                },
                                onRemove = { viewModel.removeItem(item.productId) }
                            )
                        }
                    }

                    CartSummary(
                        items = items,
                        shipping = viewModel.shippingCost,
                        onFinalize = { showDeliverySheet = true }
                    )
                }
            }
        }

        if (showDeliverySheet) {
            DeliveryOptionsSheet(
                savedAddress = viewModel.userAddress,
                savedPhone = viewModel.userPhone,
                shippingCost = viewModel.shippingCost,
                onDismiss = { showDeliverySheet = false },
                onConfirm = { deliveryType, address, phone ->
                    showDeliverySheet = false
                    viewModel.finalizeOrder(deliveryType, address, phone, onOpenWhatsApp)
                }
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
            message = stringResource(R.string.order_create_error),
            onDismiss = { viewModel.clearError() },
            onCancel = {}
        )
    }
}

@Composable
private fun CartHeader(onBackToHome: () -> Unit) {
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
                .clickable { onBackToHome() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_to_home),
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.my_cart),
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 24.sp
            ),
            color = Color.Black
        )
    }
}

@Composable
private fun EmptyCartView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.cart_empty),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.cart_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = item.imageUrl.ifEmpty { R.drawable.ic_guest },
                contentDescription = item.title,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 17.sp
                    ),
                    color = Color.Black
                )
                if (item.category.isNotEmpty()) {
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                QuantityStepper(
                    quantity = item.quantity,
                    onQuantityChange = onQuantityChange
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.remove_from_cart),
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onRemove() }
                )
                Text(
                    text = Utilities.formatMoney(item.price * item.quantity),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun QuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onQuantityChange(quantity - 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CartSummary(
    items: List<CartItem>,
    shipping: Double,
    onFinalize: () -> Unit
) {
    val subtotal = items.sumOf { it.price * it.quantity }
    val total = subtotal + shipping

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            SummaryRow(
                label = stringResource(R.string.subtotal),
                value = Utilities.formatMoney(subtotal)
            )
            if (shipping > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                SummaryRow(
                    label = stringResource(R.string.shipping),
                    value = Utilities.formatMoney(shipping)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = Utilities.formatMoney(total),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onFinalize,
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.finalize_order_whatsapp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeliveryOptionsSheet(
    savedAddress: String?,
    savedPhone: String?,
    shippingCost: Double,
    onDismiss: () -> Unit,
    onConfirm: (DeliveryType, String, String) -> Unit
) {
    val hasSavedAddress = !savedAddress.isNullOrBlank()

    var selected by remember { mutableStateOf<DeliveryType?>(null) }
    var useNewAddress by remember { mutableStateOf(!hasSavedAddress) }
    var newAddress by remember { mutableStateOf("") }
    var newAddressValid by remember { mutableStateOf(false) }

    // El teléfono es obligatorio para ambos tipos de entrega
    var phone by remember { mutableStateOf(savedPhone.orEmpty()) }
    val phoneValid = InputValidator.validatePhone(phone).isValid && phone.isNotBlank()

    val addressOk = when (selected) {
        DeliveryType.PICKUP -> true
        DeliveryType.DELIVERY -> if (useNewAddress) newAddressValid else hasSavedAddress
        null -> false
    }
    val canConfirm = selected != null && phoneValid && addressOk

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "¿Cómo quieres recibir tu pedido?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            DeliveryOptionRow(
                icon = Icons.Default.Store,
                title = "Recoger en tienda",
                subtitle = "Sin costo de envío",
                selected = selected == DeliveryType.PICKUP,
                onClick = { selected = DeliveryType.PICKUP }
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeliveryOptionRow(
                icon = Icons.Default.Home,
                title = "Enviar a domicilio",
                subtitle = if (shippingCost > 0)
                    "Envío: ${Utilities.formatMoney(shippingCost)}" else "Envío a domicilio",
                selected = selected == DeliveryType.DELIVERY,
                onClick = { selected = DeliveryType.DELIVERY }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10) phone = it.filter(Char::isDigit) },
                label = { Text("Teléfono de contacto") },
                singleLine = true,
                isError = phone.isNotBlank() && !phoneValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (selected == DeliveryType.DELIVERY) {
                Spacer(modifier = Modifier.height(16.dp))

                if (hasSavedAddress && !useNewAddress) {
                    Text(
                        text = "Domicilio guardado",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = savedAddress.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Usar otra dirección",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { useNewAddress = true }
                    )
                } else {
                    if (hasSavedAddress) {
                        Text(
                            text = "Usar domicilio guardado",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { useNewAddress = false }
                                .padding(bottom = 8.dp)
                        )
                    }
                    AddressFormFields(
                        onAddressChange = { address, valid ->
                            newAddress = address
                            newAddressValid = valid
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val type = selected ?: return@Button
                    val address = if (type == DeliveryType.DELIVERY) {
                        if (useNewAddress) newAddress else savedAddress.orEmpty()
                    } else ""
                    onConfirm(type, address, phone)
                },
                enabled = canConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DeliveryOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        RadioButton(selected = selected, onClick = onClick)
    }
}

@Composable
private fun OrderConfirmationView(
    order: ProductOrder,
    onBackToHome: () -> Unit
) {
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
                .background(Color.Transparent)
                .padding(1.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(50))
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
                text = stringResource(R.string.order_sent),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.thanks_purchase),
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
            text = stringResource(R.string.order_sent_message),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.summary).uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Text(
                    text = "#${order.orderNumber}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
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
