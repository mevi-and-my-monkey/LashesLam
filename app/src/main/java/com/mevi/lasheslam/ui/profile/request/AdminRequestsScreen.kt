package com.mevi.lasheslam.ui.profile.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items   // ← IMPORT CORRECTO
import com.mevi.lasheslam.network.CourseRequest  // ← EL MODELO CORRECTO

@Composable
fun AdminRequestsScreen(viewModel: AdminRequestsViewModel = hiltViewModel()) {

    val list = viewModel.requests
    val loading = viewModel.loading

    LaunchedEffect(Unit) {
        viewModel.loadRequests("pending")
    }

    LazyColumn {
        items(items = list) { request ->
            RequestCurseItem(
                item = request,
                onApprove = {
                    viewModel.approve(request.requestId) {
                        viewModel.loadRequests("pending")
                    }
                },
                onReject = {
                    viewModel.reject(request.requestId) {
                        viewModel.loadRequests("pending")
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = "ID: ${item.requestId}")
            Text(text = "Usuario: ${item.userId}")
            Text(text = "Curso: ${item.courseId}")
            Text(text = "Estado: ${item.status}")

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                Button(onClick = onApprove) {
                    Text("Aprobar")
                }

                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Rechazar")
                }
            }
        }
    }
}