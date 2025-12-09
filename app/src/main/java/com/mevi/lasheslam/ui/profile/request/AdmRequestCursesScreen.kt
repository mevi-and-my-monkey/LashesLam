package com.mevi.lasheslam.ui.profile.request

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.network.CourseRequest

@Composable
fun AdmRequestCursesScreen(viewModel: AdminRequestsViewModel = hiltViewModel()) {
    val list = viewModel.requests

    LaunchedEffect(Unit) {
        viewModel.loadRequests("pendiente")
    }

    LazyColumn {
        items(items = list) { request ->
            RequestCurseItem(
                item = request,
                onApprove = {
                    viewModel.approve(request.requestId) {
                        viewModel.loadRequests("pendiente")
                    }
                },
                onReject = {
                    viewModel.reject(request.requestId) {
                        viewModel.loadRequests("pendiente")
                    }
                }
            )
        }
    }
}

@Composable
fun RequestCurseItem(
    item: CourseRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // ---------- HEADER (Siempre visible) ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.courseName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.emailUser,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Ícono para expandir
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            // ---------- CONTENIDO EXPANDIBLE ----------
            AnimatedVisibility(visible = expanded) {

                Column(modifier = Modifier.padding(top = 12.dp)) {

                    Text("Nombre: ${item.nameUser}")
                    Text("Fecha: ${item.date}")
                    Text("Horario: ${item.schedule}")
                    Text("Estado: ${item.status}")

                    Spacer(modifier = Modifier.height(16.dp))

                    // ---------- BOTONES ----------
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Botón aprobar (verde pastel)
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB2E2B1) // verde pastel
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Aprobar", color = Color.Black)
                        }

                        // Botón rechazar (rojo pastel)
                        Button(
                            onClick = onReject,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC4C4) // rojo pastel
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Rechazar", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}