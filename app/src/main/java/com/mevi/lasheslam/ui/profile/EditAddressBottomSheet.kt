package com.mevi.lasheslam.ui.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.components.AddressFormFields
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditAddressBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    currentAddress: String? = null
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var address by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editar dirección", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            AddressFormFields(
                initialAddress = currentAddress,
                onAddressChange = { fullAddress, valid ->
                    address = fullAddress
                    isFormValid = valid
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        onSave(address)
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor, completa todos los campos obligatorios.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    LashesLamTheme {
        EditAddressBottomSheet({}, { })
    }
}
