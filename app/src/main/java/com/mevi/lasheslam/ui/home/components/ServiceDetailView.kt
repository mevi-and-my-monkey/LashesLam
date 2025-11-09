package com.mevi.lasheslam.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailView(
    serviceData: Map<String, Any>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            val imageUrl = serviceData["imagen"] as? String

            if (imageUrl != null) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))
            }

            Text(serviceData["titulo"].toString(), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(serviceData["descripcion"].toString(), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))

            Divider()
            Spacer(Modifier.height(8.dp))
            Text("Horario: ${serviceData["horario"]}")
            Text("Fecha: ${serviceData["fecha"]}")
            Text("Costo: ${serviceData["costo"]}")
            Text("Ubicación: ${serviceData["ubicacion"]}")
        }
    }
}