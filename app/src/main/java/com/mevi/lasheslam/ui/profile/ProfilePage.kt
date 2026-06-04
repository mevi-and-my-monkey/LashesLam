package com.mevi.lasheslam.ui.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.common.orDefault
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.ProfileOptionButton
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog

@Composable
fun ProfilePage(
    onNavigateToFavorite: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToRequestUser: () -> Unit,
    onNavigateToCourses: () -> Unit,
    onNavigateToLogOut: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user = profileViewModel.userModel
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    val isDarkMode by profileViewModel.isDarkMode.collectAsState(initial = false)
    val photoUser = profileViewModel.photoUser

    var showEditAddress by remember { mutableStateOf(false) }
    var showEditPhone by remember { mutableStateOf(false) }

    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var warningMessae by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        profileViewModel.loadUserData()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                    )
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = photoUser.ifEmpty { R.drawable.ic_guest },
                    contentDescription = stringResource(R.string.profile_picture),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.name ?: stringResource(R.string.user),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = user.email ?: "",
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stringResource(R.string.phone)} ${
                        user.phone.orDefault(
                            stringResource(
                                R.string.without_data
                            )
                        )
                    }",
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dirección: ${user.address?.takeIf { it.isNotEmpty() } ?: "Sin datos registrados"}",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dark_mode),
                        contentDescription = stringResource(R.string.dark_mode),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stringResource(R.string.dark_mode), fontSize = 18.sp)
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { profileViewModel.toggleDarkMode(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(R.string.account),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileOptionButton(
                icon = R.drawable.ic_location,
                text = stringResource(R.string.edit_address),
                onClick = { showEditAddress = true }
            )
            ProfileOptionButton(
                icon = R.drawable.ic_phone,
                text = stringResource(R.string.edit_phone),
                onClick = { showEditPhone = true }
            )
            ProfileOptionButton(
                icon = R.drawable.ic_favorite,
                text = stringResource(R.string.favorites),
                onClick = {
                    onNavigateToFavorite()
                }
            )
            ProfileOptionButton(
                icon = R.drawable.ic_orders,
                text = stringResource(R.string.orders),
                onClick = {
                    if (isAdmin) {
                        onNavigateToRequest()
                    } else {
                        onNavigateToRequestUser()
                    }
                }
            )
            if (isAdmin) {
                ProfileOptionButton(
                    icon = R.drawable.ic_inscripciones,
                    text = stringResource(R.string.inscripciones),
                    onClick = {
                        onNavigateToCourses()
                    }
                )
            }
            Button(
                onClick = {
                    warningMessae = "¿Estas seguro que quieres cerrar sesion?"
                    showWarning = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF80AB)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = stringResource(R.string.log_out),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.log_out), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }


    if (showEditAddress) {
        EditAddressBottomSheet(
            onDismiss = { showEditAddress = false },
            onSave = { address ->
                profileViewModel.updateAddress(address) { success, resultMessage ->
                    if (success) {
                        successMessage = resultMessage ?: "Dirección actualizada exitosamente"
                        showEditAddress = false
                        showSuccess = true
                    } else {
                        errorMessage = resultMessage ?: "Error al actualizar la dirección"
                        showEditAddress = false
                        showError = true
                    }
                }
            },
            currentAddress = user.address
        )
    }

    if (showEditPhone) {
        EditPhoneBottomSheet(
            onDismiss = { showEditPhone = false },
            onSave = { phone ->
                profileViewModel.updatePhone(phone) { success, resultMessage ->
                    if (success) {
                        successMessage = resultMessage ?: "Telefono actualizado exitosamente"
                        showEditPhone = false
                        showSuccess = true
                    } else {
                        errorMessage = resultMessage ?: "Error al actualizar el telefono"
                        showEditPhone = false
                        showError = true
                    }
                }
            }
        )
    }
    if (showSuccess) {
        SuccessDialog(message = successMessage, onDismiss = {
            showSuccess = false
            successMessage = ""
        }, onCancel = {})
    }

    if (showError) {
        ErrorDialog(message = errorMessage, onDismiss = {
            showError = false
            errorMessage = ""
        }, onCancel = {})
    }
    if (showWarning) {
        WarningDialog(message = warningMessae, onDismiss = {
            showWarning = false
            profileViewModel.signOut(onNavigateToLogOut)
            warningMessae = ""
        }, onCancel = {
            showWarning = false
            warningMessae = ""
        })
    }
}