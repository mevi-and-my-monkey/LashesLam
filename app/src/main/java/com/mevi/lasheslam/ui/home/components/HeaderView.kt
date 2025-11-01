package com.mevi.lasheslam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager

@Composable
fun HeaderView(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val photoUrl = firebaseUser?.photoUrl?.toString()
    val context = LocalContext.current
    val isUserInvited by SessionManager.isUserInvited.collectAsState()

    if (!isUserInvited) {
        LaunchedEffect(Unit) {
            Firebase.firestore.collection("users")
                .document(firebaseUser?.uid ?: return@LaunchedEffect)
                .get().addOnSuccessListener {
                    name = it.getString("name")?.split(" ")?.firstOrNull() ?: ""
                }
        }
    } else {
        name = "Invitado"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUrl)
                        .crossfade(true)
                        .error(R.drawable.ic_guest)
                        .placeholder(R.drawable.ic_guest)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = if (!isUserInvited) "Bienvenido de nuevo" else "Bienvenido",
                        color = Color.Black.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            IconButton(
                onClick = { /* acción carrito */ },
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Carrito de compras",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .clickable {
                    navController.navigate(Screen.Search.route)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Buscar productos...",
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
        }
    }
}